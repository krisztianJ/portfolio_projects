FOR ENGLISH VERSION SCROLL DOWN

# 🩸 Véradó Nyilvántartó Alkalmazás | Blood Donor Management App (JavaFX + H2 + JPA)
(#-blood-donor-management-application)

Egy JavaFX alapú, lokális adatbázissal működő véradó-nyilvántartó rendszer, mely támogatja a véradók és véradó pontok kezelését, keresését, módosítását és statisztikák megjelenítését.

---

## 🎯 Funkciók

- 👤 Véradók rögzítése, törlése, frissítése
- 🏥 Véradó pontok (kórházak) nyilvántartása
- 🔎 Keresés név, vércsoport, mennyiség és ID alapján
- 🕒 Nyitvatartási idő szerinti szűrés véradó pontoknál
- 🎁 Juttatások jelzése (VAN / NINCS)
- 📊 Vérkészlet statisztika minden vértípusra
- 💾 Adatmentés H2 adatbázisba JPA segítségével
- 🖼 JavaFX GUI + FXML + CSS (dark style)

---

## ⚙️ Technológiák

| Komponens | Leírás |
|----------|--------|
| JavaFX | Grafikus felhasználói felület (FXML, SceneBuilder) |
| H2 | Beépített SQL adatbázis |
| JPA | Java Persistence API ORM megoldás |
| JDBC | Manuális SQL lekérdezések |
| JUnit | Egységtesztelés |
| CSS | Felület testreszabás |

---

## 🧪 Kódrészletek

### Véradó hozzáadása:
```java
Verado s = new Verado();
s.setNev("Teszt Elek");
s.setVercsoport("A+");
s.setMennyiseg(500);
s.setKorhazID(1);
aDAO.saveVerado(s);
```

### Vércsoport szerinti összesítő:
```java
if (verado.getVercsoport().equals("A+")) {
    APlusCounter.setText(String.valueOf(APlusCounter + verado.getMennyiseg()));
}
```

---

## ▶️ Futatás

1. Indítsd el a `MainApp.java` fájlt.
2. A H2 adatbázis TCP/Web módban elindul (`Server.runTool(...)`)
3. A GUI automatikusan betöltődik.

---

## 🧪 Tesztelés

A fájl egységtesztjei lefedik a:
- időformátum validálást
- karakter/szám detektálást
- `Korhaz` és `Verado` osztály működését

---

## 🔐 Adatbázis

- `KORHAZ` tábla: ID, Név, Nyitvatartás, Juttatás, Helyszín
- `VERADO` tábla: ID, Név, Vércsoport, Mennyiség, KorhazID (kapcsolat)

---


# 🩸 Blood Donor Management Application (JavaFX + H2 + JPA)
(#-blood-donor-management-application)

A JavaFX-based local database application for managing blood donors and donation points. It supports adding, searching, updating donors and hospitals, along with displaying blood type statistics.

---

## 🎯 Features

- 👤 Add, delete, update blood donors
- 🏥 Manage donation points (hospitals)
- 🔎 Search by name, blood type, quantity, and ID
- 🕒 Filter donation points by opening hours
- 🎁 Benefit indicator (YES / NO)
- 📊 Blood supply statistics for each blood type
- 💾 Data persistence using H2 database with JPA
- 🖼 JavaFX GUI + FXML + CSS (dark theme)

---

## ⚙️ Technologies

| Component | Description |
|----------|-------------|
| JavaFX | Graphical User Interface (FXML, SceneBuilder) |
| H2 | Embedded SQL database |
| JPA | Java Persistence API (ORM) |
| JDBC | Manual SQL query execution |
| JUnit | Unit testing |
| CSS | UI customization |

---

## 🧪 Code Snippets

### Adding a new donor:
```java
Verado s = new Verado();
s.setNev("Teszt Elek");
s.setVercsoport("A+");
s.setMennyiseg(500);
s.setKorhazID(1);
aDAO.saveVerado(s);
```

### Blood type statistics:
```java
if (verado.getVercsoport().equals("A+")) {
    APlusCounter.setText(String.valueOf(APlusCounter + verado.getMennyiseg()));
}
```

---

## ▶️ How to Run

1. Launch `MainApp.java`
2. The H2 database will start in TCP/Web mode (`Server.runTool(...)`)
3. The GUI will load automatically

---

## 🧪 Testing

Unit tests cover:
- Time format validation
- Character/number detection logic
- Functionality of `Korhaz` and `Verado` classes

---

## 🔐 Database Schema

- `KORHAZ` table: ID, Name, Opening Hours, Benefits, Location
- `VERADO` table: ID, Name, Blood Type, Quantity, KorhazID (relationship)

