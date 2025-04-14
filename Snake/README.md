FOR ENGLISH VERSION SCROLL DOWN

# 🐍 Snake Játék (Python, Tkinter + Pygame)

Egy funkciókban gazdag Snake játék, amely **Python** nyelven készült, **Tkinter** grafikus felülettel és **Pygame** hangkezeléssel.  
Ez a verzió kibővíti a klasszikus kígyós játék mechanikáját testreszabható megjelenéssel, nehézségi szintekkel, akadályokkal, mentési/betöltési lehetőséggel és hanghatásokkal.

---

## 🎮 Funkciók

- ✅ Többféle nehézségi szint: `Könnyű`, `Közepes`, `Nehéz`, `Ultra`  
- 🎨 Testreszabható kígyószín (színválasztó segítségével)  
- 🔈 Hanghatások és háttérzene (Pygame mixerrel)  
- 💾 Játékállás mentése és betöltése JSON formátumban  
- 🧱 Véletlenszerű akadályok az `Ultra` nehézségi szinten  
- ⏸ Szüneteltetés / folytatás (`P`), gyors újrakezdés (`R`), mentés (`S`), betöltés (`L`)  
- 📊 Pontszám és rekord nyilvántartás (helyi fájlban)  
- 📐 Reszponzív vászon és intuitív kezelőfelület minden felhasználói szinthez  

---

## 🛠️ Felhasznált technológiák

| Komponens        | Leírás                                |
|------------------|----------------------------------------|
| `tkinter`        | Grafikus felület (menük, gombok, vászon) |
| `pygame`         | Hangkezelés (zene, effektek)           |
| `random`         | Étel és akadály generálása            |
| `json`           | Játékállás mentése/betöltése           |
| `colorchooser`   | Kígyószín kiválasztása                |
| `simpledialog`   | Nehézségi szint kiválasztása párbeszédablakból |

---

## 🧪 A játékmenet logikája

### Mozgás és ciklus:
```python
def next_turn(self):
    if self.paused:
        return
    # A kígyó mozgatása az aktuális irány alapján
    # Új fej hozzáadása, étel ellenőrzése
    # Pontszám frissítése vagy farok törlése
    # Ütközésvizsgálat, különben következő ciklus ütemezése
```


# 🐍 Snake Game (Python, Tkinter + Pygame)

A feature-rich Snake game developed in **Python**, using **Tkinter** for the graphical interface and **Pygame** for audio support.  
This version extends the classic snake mechanics with customizable visuals, difficulty levels, obstacles, game saving/loading, and sound effects.

---

## 🎮 Features

- ✅ Multiple difficulty levels: `Easy`, `Medium`, `Hard`, `Ultra`  
- 🎨 Customizable snake color via color picker  
- 🔈 Sound effects and background music (Pygame mixer)  
- 💾 Save and load game state from JSON  
- 🧱 Random obstacles on `Ultra` difficulty  
- ⏸ Pause / resume (`P`), quick restart (`R`), save (`S`), load (`L`)  
- 📊 Score and high score tracking (local text file)  
- 📐 Responsive canvas and intuitive UI for any user level  

---

## 🛠️ Technologies Used

| Component       | Description                     |
|----------------|---------------------------------|
| `tkinter`      | GUI (menus, buttons, canvas)    |
| `pygame`       | Sound handling (music, effects) |
| `random`       | Food and obstacle generation    |
| `json`         | Save/load system state          |
| `colorchooser` | Snake color selection           |
| `simpledialog` | Difficulty selection dialog     |

---

## 🧪 Game Flow Logic

### Movement & Loop:
```python
def next_turn(self):
    if self.paused:
        return
    # Move snake based on current direction
    # Add new head, check for food
    # Update score or remove tail
    # Check for collision, else schedule next frame
