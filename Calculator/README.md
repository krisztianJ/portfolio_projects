FOR ENGLISH VERSION SCROLL DOWN
# 🧮 Számológép (Python, Tkinter)

Egy modern, letisztult és funkciókban gazdag számológép alkalmazás **Python** és **Tkinter** segítségével.  
Támogatja a klasszikus műveleteket, valamint tudományos funkciókat, témaváltást, tooltipeket és előzmények mentését is.

---

## 🎯 Funkciók

- ➕ Alapműveletek: összeadás, kivonás, szorzás, osztás  
- 🧪 Tudományos funkciók: sin, cos, √, log, ln, π, e, hatványozás  
- 🌙 Témaváltás: világos / sötét mód  
- 🧠 Tooltip súgók a gombokhoz  
- 💾 Előzmények mentése fájlba (`calculator_history.txt`)  
- 📜 Előzmények megjelenítése az ablakban (utolsó 5 számítás)  
- ⌨️ Billentyűzet támogatás (Enter, Backspace, számok, operátorok)  
- 📐 Nem átméretezhető, fix méretű GUI (600x700 px)

---

## 🛠️ Felhasznált technológiák

| Komponens      | Leírás                                 |
|----------------|-----------------------------------------|
| `Python`       | Programozási nyelv                      |
| `Tkinter`      | Beépített GUI könyvtár                  |
| `math`         | Tudományos műveletek kezelése           |
| `messagebox`   | Hibaüzenetek megjelenítése              |
| `eval()`       | Kifejezések kiértékelése sztringből     |
| `file I/O`     | Előzmények fájlba mentése               |

---

## 🧪 Példakód: Kifejezés kiértékelése
```python
def evaluate(self):
    try:
        result = str(eval(self.calculation))
        expression = f"{self.calculation} = {result}"
        self.history.append(expression)
        self.save_to_file(expression)
        self.update_history()
        self.calculation = result
    except Exception as e:
        messagebox.showerror("Hiba", f"Hibás kifejezés: {e}")
        self.calculation = ""
    self.update_display()
```

# 🧮 Calculator (Python, Tkinter)

A modern and feature-rich calculator app built using **Python** and **Tkinter**.  
Supports basic and scientific operations, theming, tooltips, history saving and keyboard input.

---

## 🎯 Features

- ➕ Basic operations: add, subtract, multiply, divide  
- 🧪 Scientific functions: sin, cos, √, log, ln, π, e, exponentiation  
- 🌙 Light / dark theme toggle  
- 🧠 Tooltips for buttons  
- 💾 Save history to file (`calculator_history.txt`)  
- 📜 In-app display of recent 5 calculations  
- ⌨️ Full keyboard support (Enter, Backspace, digits, operators)  
- 📐 Fixed window size (600x700 px)

---

## 🛠️ Technologies Used

| Component       | Description                             |
|------------------|-----------------------------------------|
| `Python`         | Programming language                    |
| `Tkinter`        | Built-in GUI toolkit                    |
| `math`           | Scientific operations and constants     |
| `messagebox`     | Error message handling                  |
| `eval()`         | Dynamic expression evaluation           |
| `file I/O`       | Saving history to external file         |

---

## 🧪 Sample: Evaluate Expression
```python
def evaluate(self):
    try:
        result = str(eval(self.calculation))
        expression = f"{self.calculation} = {result}"
        self.history.append(expression)
        self.save_to_file(expression)
        self.update_history()
        self.calculation = result
    except Exception as e:
        messagebox.showerror("Error", f"Invalid expression: {e}")
        self.calculation = ""
    self.update_display()
```
