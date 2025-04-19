#Könyvtárak importálása
import tkinter as tk #GUI létrehozásához
from tkinter import messagebox #Hiba üzenet megjelenítéséhez
import math #Matematikai műveletekhez
import os #Fájlkezeléshez

#Számológép osztály
class Calculator:
    def __init__(self, root):
        self.root = root #Fő tkinter ablak
        self.root.title("Számológép") #Az ablak címe
        self.root.geometry("600x700") #A felbontása
        self.root.resizable(False, False) #Ne lehessen méretezhető (se vízszintesen, se függőlegesen)
        self.calculation = "" #Aktuális művelet tárolása (kezdetben üres)"
        self.history = [] #Számítások előzményei kezdetben üres tömb
        self.dark_mode = False #Dark mode-ban induljon vagy sem
        self.scientific_mode = True #Tudományos módban induljon vagy sem
        self.history_file = "calculator_history.txt" #Milyen fájlt használjon fel az előzményekhez
        self.buttons = [] #A sima gombokat tárólja
        self.scientific_buttons = [] #A tudományos gombokat tárólja (sin, cos, log stb.)

        self.create_widgets() # Meghívja azt a metódust, amely az összes vizuális komponenst (gombokat, mezőket) létrehozza és megjeleníti az ablakban.
        self.bind_keys() #Ez a metódus beköti a billentyűzetes vezérlést (pl. Enter = számítás, Backspace = törlés, számok, +, -, /, * stb. billentyűk működjenek).

    def create_widgets(self):
        # Információs sáv gombokhoz
        self.tooltip = tk.StringVar() #Dinamikusan frissíthető szöveget tárol (tooltip üzenet)
        self.tooltip_label = tk.Label(self.root, textvariable=self.tooltip, font=("Arial", 10), anchor="w")
        # cimke ami tooltip szövegét mutatja
        self.tooltip_label.pack(fill="x", padx=10, pady=(0, 5))#

        # Felsővezérlő gombok(témaváltás stb.)
        top_frame = tk.Frame(self.root)
        top_frame.pack(fill="x", padx=10, pady=(10, 0))

        #Téma váltás
        self.theme_btn = tk.Button(top_frame, text="🌙", command=self.toggle_theme, font=("Arial", 12)) #Egy frame ami vízszintesen rendezi a gombokat
        self.theme_btn.pack(side="right")

        #Tudományos nézet gomb
        self.mode_btn = tk.Button(top_frame, text="Tud. nézet: be", command=self.toggle_mode, font=("Arial", 12))
        self.mode_btn.pack(side="left")

        # Kijelző mező az eredmény és előzmény megjelenítésére
        self.display = tk.Entry(self.root, font=("Arial", 24), bd=10, relief="sunken", justify="right")#Nagy betűméret, jobb oldalra igazítva
        self.display.pack(fill="x", padx=10, pady=10)

        # Előzmények (utolso 5 számítás)
        self.history_display = tk.Text(self.root, height=4, font=("Arial", 10), bg="#f4f4f4", state="disabled") #State disabled, hogy ne lehessen szerkeszteni
        self.history_display.pack(fill="x", padx=10, pady=(0, 10))

        # "Tooltip" definíciók (dictionary-ben tárolva)
        self.tooltips = {
            "sin": "Szinusz (fokban)",
            "cos": "Koszinusz (fokban)",
            "√": "Négyzetgyök",
            "^": "Hatványozás",
            "log": "Logaritmus (10-es alap)",
            "ln": "Természetes logaritmus",
            "π": "Pi értéke",
            "e": "Euler szám",
            "÷": "Osztás",
            "×": "Szorzás",
            "−": "Kivonás",
            "⌫": "Törlés",
            "C": "Törlés minden",
            "=": "Számítás"
        }

        # Gomb definíciók
        btns = [
            ('7', '8', '9', '÷'),
            ('4', '5', '6', '×'),
            ('1', '2', '3', '−'),
            ('0', '.', '(', ')'),
            ('⌫', 'C', '=', '+')
        ]

        #Tudományos gombok
        sci_btns = [
            ('sin', 'cos', '√', '^'),
            ('log', 'ln', 'π', 'e')
        ]

        self.btn_frame = tk.Frame(self.root)
        self.btn_frame.pack()

        #Gombok legenerálása (számok)
        for row_index, row in enumerate(btns):
            for col_index, symbol in enumerate(row):
                btn = tk.Button(self.btn_frame, text=symbol, width=8, height=2, font=("Arial", 14),
                                command=lambda s=symbol: self.on_button_click(s)) #Biztosítja, hogy a gomb kattintás a megfelelő szimbolumot küldje
                btn.grid(row=row_index, column=col_index, padx=3, pady=3)
                btn.bind("<Enter>", lambda e, b=btn, s=symbol: self.show_tooltip(s, b)) #Tooltip események
                btn.bind("<Leave>", lambda e, b=btn: self.hide_tooltip(b))#hozzákötése
                btn.configure(relief="flat")
                self.buttons.append(btn)

        self.sci_frame = tk.Frame(self.root)
        self.sci_frame.pack()

        # Gombok legenerálása (Tudományos)
        #Hasonló logika mint az előző ciklusban
        for row_index, row in enumerate(sci_btns):
            for col_index, symbol in enumerate(row):
                btn = tk.Button(self.sci_frame, text=symbol, width=8, height=2, font=("Arial", 14),
                                command=lambda s=symbol: self.on_button_click(s))
                btn.grid(row=row_index, column=col_index, padx=3, pady=3)
                btn.bind("<Enter>", lambda e, b=btn, s=symbol: self.show_tooltip(s, b))
                btn.bind("<Leave>", lambda e, b=btn: self.hide_tooltip(b))
                btn.configure(relief="flat")
                self.scientific_buttons.append(btn)

    #Ki/be kapcsolja a tudományos nézetet
    def toggle_mode(self):
        #Egyfajta toggle kapcsoló, a jelenlegi mód ellentetjét veszi
        self.scientific_mode = not self.scientific_mode
        if self.scientific_mode: #Ha tudományos nézetben van
            self.sci_frame.pack() #Elhelyezi a tudományos gombokat
            self.mode_btn.configure(text="Tud. nézet: be") #Kiírja, hogy jelenleg a tudományos módban van
        else:
            self.sci_frame.forget() #Eltávolítja a helyét
            self.mode_btn.configure(text="Tud. nézet: ki") #Kiiírja, hogy nincs bekapcsolva

    #Tooltip mutatása az egér rávitelekor
    def show_tooltip(self, symbol, button):
        if symbol in self.tooltips: #Ellenőrzi, hogy van-e tooltip szöveg a symbol-hoz rendelve
            self.tooltip.set(self.tooltips[symbol]) # Beállítja a tooltip szöveget, ami az Entry feletti kis sávban jelenik meg
        button.configure(relief="raised") #Ez egy kis vizuális kiemelés: a gomb kinézete kicsit kiemelkedik, amikor az egér fölötte van (raised stílus)

    #Tooltip elrejtése
    def hide_tooltip(self, button):
        self.tooltip.set("") #Egyszerűen kiüríti a tooltip szöveget, így a felhasználó nem lát semmit a tooltip mezőben
        button.configure(relief="flat") # Visszaállítja a gomb kinézetét eredeti, lapos stílusúra (flat), vagyis megszünteti a kiemelést

    #A különleges jelekhez rendel matematikai logikát
    def on_button_click(self, symbol):
        if symbol in ["C", "CLR"]:
            self.calculation = ""
        elif symbol in ["⌫", "DEL"]:
            self.calculation = self.calculation[:-1]
        elif symbol == "=":
            self.evaluate()
            return
        elif symbol == "√":
            self.calculation += "math.sqrt("
        elif symbol == "sin":
            self.calculation += "math.sin(math.radians("
        elif symbol == "cos":
            self.calculation += "math.cos(math.radians("
        elif symbol == "log":
            self.calculation += "math.log10("
        elif symbol == "ln":
            self.calculation += "math.log("
        elif symbol in ["pi", "π"]:
            self.calculation += "math.pi"
        elif symbol == "e":
            self.calculation += "math.e"
        elif symbol == "^":
            self.calculation += "**"
        elif symbol == "÷":
            self.calculation += "/"
        elif symbol == "×":
            self.calculation += "*"
        elif symbol == "−":
            self.calculation += "-"
        elif symbol == "(":
            self.calculation += "("
        elif symbol == ")":
            self.calculation += ")"
        else:
            self.calculation += symbol

        if self.calculation.endswith("(") and any(self.calculation.endswith(f) for f in ["log10(", "log(", "sin(", "cos(", "sqrt("]):
            self.calculation += ")"

        self.update_display()

    #Kiértékeli az aktuális kifejezést
    def evaluate(self):
        try: #Hiba kezelés kezdete
            result = str(eval(self.calculation))#A beépített füvénnyel kiszámoljuk és stringgé alakítjuk, hogy kitudjuk iratni
            expression = f"{self.calculation} = {result}" #Formázott szöveg
            self.history.append(expression) #Hozzáadjuk az előzmény listához
            self.save_to_file(expression) #Fájlba mentés
            self.update_history() #Frissíti az előzmény ablakot
            self.calculation = result #Elmentjük az eredményt, hogy további műveleteket tudjunk vele folytatni
        except Exception as e: #Ha try rész hibát észlel például (5+*2)
            messagebox.showerror("Hiba", f"Hibás kifejezés: {e}") #Hibás szintaxis esetén hiba üzenet
            self.calculation = "" #Aktuális kifejezés törlése
        self.update_display() #Kijelző mező frissítéses

    #Törli az Entry mezőt majd újra beírja a kifejezést
    def update_display(self):
        self.display.delete(0, tk.END) # A kezdő értéktől a végéig tőrli a mezőt
        self.display.insert(0, self.calculation) #Beírja a számítás eredményét

    #Az elözmény frissítése
    def update_history(self):
        self.history_display.config(state="normal") #Mivel a Text mezőt alapból "disabled" állapotban tartjuk (hogy ne lehessen beleírni kézzel), előbb engedélyezni kell az írást
        self.history_display.delete(1.0, tk.END) #Teljesen kiüríti az előző tartalmat a Text mezőből
        for line in self.history[-5:]: #Végigmegy a self.history lista utolsó 5 elemén (a legfrissebb műveletek)
            self.history_display.insert(tk.END, line + "\n") #Egyenként hozzáadja őket az előzmények mezőhöz, soronként
        self.history_display.config(state="disabled") #Ismét letiltja a mezőt, ne lehessen bele írni

    #Fájlba írja a sorokat
    def save_to_file(self, expression):
        try: #Hibakezelés kezdete
            with open(self.history_file, "a") as file: #Megnyitja a fájlt append módban (self.history_file = elérési út), a with lezárja a fájlt ha végeztünk
                file.write(expression + "\n") #Minden számítás külön sorban
        except Exception as e: #Ha valami hiba történik
            print(f"Nem sikerült menteni a fájlba: {e}")

    #Billentyűzet kezelés
    def bind_keys(self):
        self.root.bind("<Return>", lambda event: self.evaluate()) #Enter esetén az eval függvény fut le
        self.root.bind("<BackSpace>", lambda event: self.on_button_click("DEL")) #backspace billentyű esetén a törlés gomb fut le
        self.root.bind("<Key>", self.key_input) # Általános gomb figyelés például 7

    #Billentyű lenyomása esetén
    def key_input(self, event):
        if event.char in '0123456789+-*/().': #Megnézi, hogy érvényes matematikai karakter-e
            self.calculation += event.char #Hozzáadja a kifejezéshez
            self.update_display() #Kijelző frissítése

    #Világos sötét mód váltás
    def toggle_theme(self):
        self.dark_mode = not self.dark_mode #Toggle kapcsoló, az ellentetjét veszi a jelenlegi állapotnak
        bg = "#2e2e2e" if self.dark_mode else "#ffffff" #Színek beállíása
        fg = "#ffffff" if self.dark_mode else "#000000"
        btn_bg = "#444444" if self.dark_mode else "#f0f0f0"

        self.root.configure(bg=bg) #Ablak háttérszínét állítja
        self.display.configure(bg=bg, fg=fg, insertbackground=fg) #Kijelző mező színét állítja
        self.history_display.configure(bg=bg, fg=fg) #Előzmények mező színét állítja
        self.tooltip_label.configure(bg=bg, fg=fg) #Tooltip mező színét változtatja
        self.theme_btn.configure(bg=bg, fg=fg, text="☀️" if self.dark_mode else "🌙") #A témaváltó gomb színe és emojija is változik
        self.mode_btn.configure(bg=bg, fg=fg) #Tudományos nézet gomb színe is frissül

        for btn in self.buttons + self.scientific_buttons: #Minden gomb megkapja a megfelelő háttérszínt
            btn.configure(bg=btn_bg, fg=fg)

#Főprogram
if __name__ == "__main__":
    root = tk.Tk()
    app = Calculator(root)
    root.mainloop()
