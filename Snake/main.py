#---Könyvtárak importálása---
import tkinter as tk #Grafikus felülethez
import random #Véletlenszerű szám generáláshiz
from tkinter import simpledialog, colorchooser #Felhasználói inputhoz
import json #Mentés/betöltés fájlból
import pygame #Hang lejátszásához

# --- Konstansok ---
GAME_WIDTH = 550 #Játék szélesség
GAME_HEIGHT = 500 #Játék magasság
SPACE_SIZE = 50 #Rácsegység
BODY_PARTS = 3 #Kígyó kezdő mérete
SNAKE_COLOR = "green" #Alap kígyó szín
FOOD_COLOR = "red" #Kaja szín
BACKGROUND_COLOR = "black" #Háttér szín
OBSTACLE_COLOR = "gray" #Akadály szín

# --- Kígyó osztály ---
class Snake:
    def __init__(self, canvas, color):
        self.canvas = canvas #A vászon amire rajzol
        self.body_size = BODY_PARTS #Ekkora méretet rajzol
        self.coordinates = [] #A kígyó testének koordinátáít tárolja listában
        self.squares = [] #A kirajzolt négyzet listája

        #Létrehoz 3db [0,0] poziciójú test darabot
        for _ in range(self.body_size):
            self.coordinates.append([0, 0])

        for x, y in self.coordinates:
            #Létrehozza a négyzeteket
            square = self.canvas.create_rectangle(x, y, x + SPACE_SIZE, y + SPACE_SIZE, fill=color, tags="snake")
            #Majd hozzáadja a listához
            self.squares.append(square)

# --- Kaja osztály ---
class Food:
    def __init__(self, canvas):
        #Egy random helyre rakja a képernyőn a kaját
        x = random.randint(0, (GAME_WIDTH // SPACE_SIZE) - 1) * SPACE_SIZE
        y = random.randint(0, (GAME_HEIGHT // SPACE_SIZE) - 1) * SPACE_SIZE
        self.coordinates = [x, y] #A kaja aktuális pozíciója
        #Itt rajzolja meg magát a kaját
        canvas.create_oval(x, y, x + SPACE_SIZE, y + SPACE_SIZE, fill=FOOD_COLOR, tags="food")

# --- Játék osztály ---
class Game:
    def __init__(self, root):
        self.root = root #Tkinter főablak
        self.root.title("🐍 Snake Játék") #Cím beállítása
        self.root.configure(bg="#222") #Háttér beállítása
        self.root.resizable(False, False) #Ne lehessen állítani a méreten
        self.difficulty_level = "könnyű" #Kezdésnek a difficulty könnyű legyen
        self.snake_color = SNAKE_COLOR #Kígyó szín
        self.main_menu() #Meghívja a main_menu() függvényt

#--GUI menüpontokat hoz létre és hozzájuk rendel eseménykezelőket--
    def main_menu(self):
        self.load_high_score() #Betölti a rekord pontot
        self.menu_frame = tk.Frame(self.root, bg="#222")
        self.menu_frame.pack(padx=20, pady=20)

        #Főcím
        title = tk.Label(self.menu_frame, text="🐍 Snake Játék", font=("Arial", 32, "bold"), fg="white", bg="#222")
        title.pack(pady=15)

        #Rekord kiírása
        high_score_label = tk.Label(self.menu_frame, text=f"🔝 Rekord: {self.high_score} pont", font=("Arial", 16), fg="lightgreen", bg="#222")
        high_score_label.pack(pady=(0, 15))

        #Játék indítása gomb
        start_button = tk.Button(self.menu_frame, text="▶️ Játék indítása", font=("Arial", 16), width=20, command=self.start_game)
        start_button.pack(pady=5)

        #Nehézség kiválasztása gomb
        difficulty_button = tk.Button(self.menu_frame, text="⚙️ Nehézség kiválasztása", font=("Arial", 16), width=20, command=self.select_difficulty)
        difficulty_button.pack(pady=5)

        #Kígyószín kiválasztása gomb
        color_button = tk.Button(self.menu_frame, text="🎨 Kígyószín kiválasztása", font=("Arial", 16), width=20, command=self.select_snake_color)
        color_button.pack(pady=5)

        #Játék betöltése gomb
        load_button = tk.Button(self.menu_frame, text="💾 Játék betöltése", font=("Arial", 16), width=20, command=self.load_saved_game_from_menu)
        load_button.pack(pady=5)

        #Kilépés gomb
        quit_button = tk.Button(self.menu_frame, text="❌ Kilépés", font=("Arial", 16), width=20, command=self.root.destroy)
        quit_button.pack(pady=5)

    #Nehézség kiválasztása
    def select_difficulty(self):
        #Bekér egy szintet és elmenti sztringbe
        level = simpledialog.askstring("Nehézségi szint", "Válassz szintet: könnyű / közepes / nehéz / ultra")
        if level:
            self.difficulty_level = level.lower()

    #Kígyó szín kiválasztása színválasztóval
    def select_snake_color(self):
        color = colorchooser.askcolor(title="Válassz kígyószínt")[1]
        if color:
            self.snake_color = color

    def start_game(self):
        self.menu_frame.destroy() #Menü törlése
        self.initialize_game() #Játék inicializálása, előkészíti a szükséges objektumokat
        self.new_game() #Új játék indítása, mindent alaphelyzetbe állít

    def initialize_game(self):
        pygame.mixer.init() #Hangok betöltése
        pygame.mixer.music.load("background_music.mp3") #Háttér zene
        pygame.mixer.music.set_volume(0.2) # Háttér zene hangja 20%-ra csökken
        pygame.mixer.music.play(-1) #Végtelenítve megy a zene
        self.eat_sound = pygame.mixer.Sound("eat.mp3") #Kaja evés hang beállítása
        self.game_over_sound = pygame.mixer.Sound("gameover.mp3") #Játék vége hang beállítása
        #Állapotváltozók
        self.score = 0
        self.high_score = 0
        #Kezdő írány (melyik irányba induljon a kígyó)
        self.direction = "down"
        #Kezdő sebesség
        self.speed = 100
        #Ne legyen megállítva a jták
        self.paused = False
        #Ne legyenek akadályok
        self.obstacles = []
        #Ne legyen ultra hard
        self.ultra_hard = False
        #Rekord betöltése
        self.load_high_score()
        #Nehézségi szint beállítása
        self.apply_difficulty()

        #Pontszám kijelzése
        self.label = tk.Label(self.root, text=f"Pont: 0 | Rekord: {self.high_score}", font=("Arial"))
        self.label.pack()

        #Hangerő csúszka
        self.volume_slider = tk.Scale(self.root, from_=0, to=100, orient="horizontal", label="Zene hangereje",
                                      command=self.set_volume)
        #Alapból 20-as hangerőn kezdd
        self.volume_slider.set(20)
        self.volume_slider.pack()

        #Vászon létrehozása
        self.canvas = tk.Canvas(self.root, bg=BACKGROUND_COLOR, height=GAME_HEIGHT, width=GAME_WIDTH)
        self.canvas.pack()

        #Ablak középre pozícionálása
        self.root.update()
        window_width = self.root.winfo_width()
        window_height = self.root.winfo_height()
        screen_width = self.root.winfo_screenwidth()
        screen_height = self.root.winfo_screenheight()
        x = int((screen_width / 2) - (window_width / 2))
        y = int((screen_height / 2) - (window_height / 2))
        self.root.geometry(f"{window_width}x{window_height}+{x}+{y}")

        #Billentyűzet események hozzá rendelése
        self.root.bind("<Left>", lambda event: self.change_direction("left"))
        self.root.bind("<Right>", lambda event: self.change_direction("right"))
        self.root.bind("<Up>", lambda event: self.change_direction("up"))
        self.root.bind("<Down>", lambda event: self.change_direction("down"))
        self.root.bind("r", lambda event: self.new_game())
        self.root.bind("s", lambda event: self.save_game())
        self.root.bind("l", lambda event: self.load_game())
        self.root.bind("p", lambda event: self.toggle_pause())

    def load_saved_game_from_menu(self):
        self.menu_frame.destroy()
        self.initialize_game()
        self.load_game()

    #Új játék
    def new_game(self):
        self.canvas.delete("all") #Törli a vászonról az összes dolgot
        self.score = 0 #Vissza állítja a pontot
        self.direction = "down" #Vissza állítja az irányt
        self.label.config(text=f"Pont: {self.score} | Rekord: {self.high_score}")
        self.snake = Snake(self.canvas, self.snake_color) #Új Kígyó példány
        self.food = Food(self.canvas) #Új Kaja példány
        if self.ultra_hard: #Ha be van kapcsolva az ultra nehézség
            self.create_obstacles() # akkor legenerálja az akadályokat
        self.next_turn() #Elindítja a főciklust

    #A sebességet állítja a kiválasztott nehézség alapján
    def apply_difficulty(self):
        level = self.difficulty_level
        if level == "közepes":
            self.speed = 75
        elif level == "nehéz":
            self.speed = 60
        elif level == "ultra":
            self.speed = 80
            self.ultra_hard = True
        else:
            self.speed = 100

    #A pygame zene hangerejét állítja
    def set_volume(self, val):
        volume = int(val) / 100
        pygame.mixer.music.set_volume(volume)

    #JSON formátumba menti a játék jelenlegi állítását
    def save_game(self):
        state = {
            "snake": self.snake.coordinates,
            "food": self.food.coordinates,
            "score": self.score,
            "high_score": self.high_score,
            "direction": self.direction,
            "obstacles": self.obstacles,
            "paused": self.paused,
            "ultra_hard": self.ultra_hard,
            "snake_color": self.snake_color,
            "difficulty_level": self.difficulty_level
        }
        with open("savegame.json", "w") as f:
            json.dump(state, f)

    #Játék betöltése JSON fájlból
    def load_game(self):
        try:
            with open("savegame.json", "r") as f:
                state = json.load(f)

            # Vászon törlése
            self.canvas.delete("all")

            # Állapotok visszatöltése
            self.snake_color = state.get("snake_color", SNAKE_COLOR)
            self.snake_coordinates = state["snake"]
            self.food_coordinates = state["food"]
            self.score = state["score"]
            self.high_score = state["high_score"]
            self.direction = state["direction"]
            self.paused = state.get("paused", False)
            self.ultra_hard = state.get("ultra_hard", False)
            self.difficulty_level = state.get("difficulty_level", "könnyű")
            self.apply_difficulty()

            # Kígyó újragenerálása a mentett koordináták alapján
            self.snake = type('Snake', (), {})()  # Üres objektum (nem hívja meg az __init__-et)
            self.snake.coordinates = self.snake_coordinates
            self.snake.squares = []
            for x, y in self.snake.coordinates:
                square = self.canvas.create_rectangle(x, y, x + SPACE_SIZE, y + SPACE_SIZE,
                                                      fill=self.snake_color, tags="snake")
                self.snake.squares.append(square)

            # Ételt újra kirajzoljuk a mentett pozícióra
            self.food = type('Food', (), {})()
            self.food.coordinates = self.food_coordinates
            self.canvas.create_oval(self.food.coordinates[0], self.food.coordinates[1],
                                    self.food.coordinates[0] + SPACE_SIZE, self.food.coordinates[1] + SPACE_SIZE,
                                    fill=FOOD_COLOR, tags="food")

            # Akadályok kirajzolása
            self.obstacles = state.get("obstacles", [])
            for x, y in self.obstacles:
                self.canvas.create_rectangle(x, y, x + SPACE_SIZE, y + SPACE_SIZE,
                                             fill=OBSTACLE_COLOR, tags="obstacle")

            # Pontszám kijelzés frissítése
            self.label.config(text=f"Pont: {self.score} | Rekord: {self.high_score}")

            # Újraindítjuk a ciklust
            self.next_turn()

        except FileNotFoundError:
            pass

    #Szünet be/ki kapcsolása
    def toggle_pause(self):
        self.paused = not self.paused
        if not self.paused:
            self.next_turn()

    #Kígyó irányításának beállítása
    def change_direction(self, new_direction):
        opposite_directions = {"left": "right", "right": "left", "up": "down", "down": "up"}
        if new_direction != opposite_directions.get(self.direction):
            self.direction = new_direction

    #Akadályok létrehozása
    def create_obstacles(self):
        for _ in range(4):
            x = random.randint(2, (GAME_WIDTH // SPACE_SIZE) - 3) * SPACE_SIZE
            y = random.randint(2, (GAME_HEIGHT // SPACE_SIZE) - 3) * SPACE_SIZE
            self.canvas.create_rectangle(x, y, x + SPACE_SIZE, y + SPACE_SIZE, fill=OBSTACLE_COLOR, tags="obstacle")
            self.obstacles.append((x, y))

    #Frissíti a pontszám kijelzést
    def update_score(self):
        self.label.config(text=f"Pont: {self.score} | Rekord: {self.high_score}")

    #A játék fő ciklusa
    def next_turn(self):
        #Ha szünet van akkor nem történik semmi
        if self.paused:
            return

        #A kígyó fejének aktuális poziciójának lekérdezése
        x, y = self.snake.coordinates[0]

        #Új pozíció kiszámítása a mozgás irány alapján
        if self.direction == "up":
            y -= SPACE_SIZE
        elif self.direction == "down":
            y += SPACE_SIZE
        elif self.direction == "left":
            x -= SPACE_SIZE
        elif self.direction == "right":
            x += SPACE_SIZE

        #Az új pozíciót beilleszti a koordináta lista elejére és ez lesz az új fej.
        self.snake.coordinates.insert(0, (x, y))
        #Új testdarabot rajzol az új fejhelyre, és hozzáadja a négyzetek listájához
        square = self.canvas.create_rectangle(x, y, x + SPACE_SIZE, y + SPACE_SIZE, fill=self.snake_color, tags="snake")
        self.snake.squares.insert(0, square)

        #Megnézi, hogy evett-e (fej a kaja pozícióban)
        if x == self.food.coordinates[0] and y == self.food.coordinates[1]:
            self.score += 1#Növeli a pontszámot
            #Megnézi, hogy az új pont az rekord-e
            if self.score > self.high_score:
                self.high_score = self.score
                self.save_high_score()
            #Frissíti a pontokat
            self.update_score()
            self.eat_sound.play() #Lejátsza az evés hangot
            self.canvas.delete("food") #Tőrli a kaját a vászonról
            self.food = Food(self.canvas) # Új kaja generálása
        else:
            del self.snake.coordinates[-1] # Ha nem evett akkor a kígyó végét
            self.canvas.delete(self.snake.squares[-1]) # töri a listából és a képernyőről
            del self.snake.squares[-1]# Így a pozíciója változik

        #Megnézi, hogy van e ütközés
        if self.check_collisions():
            #Ha igen akkor vége a játéknak
            self.game_over()
        else:
            #Ha nem akkor meghívja újra a függvényt (next_turn)
            self.root.after(self.speed, self.next_turn)

    #Megnézi, történt-e ütközés
    def check_collisions(self):
        x, y = self.snake.coordinates[0]

        #Ha a falnak ütközik a fej, tehát elhagyja a játék teret
        if x < 0 or x >= GAME_WIDTH or y < 0 or y >= GAME_HEIGHT:
            return True
        #Ha magának ütközik
        if (x, y) in self.snake.coordinates[1:]:
            return True
        #Ha akadálynak ütközik
        if (x, y) in self.obstacles:
            return True
        #Ha egyik sem akkor folytatódik a játék.
        return False

    #Játék vége
    def game_over(self):
        #Játék vége hang lejátszása
        self.game_over_sound.play()
        #Törli a jelenlegi képernyőt
        self.canvas.delete("all")
        #Kiírja a Vesztettél, nyomd meg az R betűt stb..
        self.canvas.create_text(GAME_WIDTH / 2, GAME_HEIGHT / 2, font=("Arial", 50), text="VESZTETTÉL", fill="red")
        self.canvas.create_text(GAME_WIDTH / 2, GAME_HEIGHT / 2 + 50, font=("Arial", 25), text="Nyomd meg az R betűt az új játékhoz", fill="red")

    #Rekord betöltése
    def load_high_score(self):
        try:
            with open("highscore.txt", "r") as f:
                self.high_score = int(f.read())
        except:
            self.high_score = 0
    #Rekord mentése
    def save_high_score(self):
        with open("highscore.txt", "w") as f:
            f.write(str(self.high_score))

#GUI ablak létrehozása és Game példány indítása
if __name__ == "__main__":
    root = tk.Tk()
    game = Game(root)
    root.mainloop()
