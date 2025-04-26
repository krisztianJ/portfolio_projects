FOR ENGLISH VERSION SCROLL DOWN

# ⚠️ FONTOS MEGJEGYZÉS  
A forráskódot nem töltöm fel ide, de egy futtatható fájlként el fogom helyezni a programot, csak ez némi átdolgozást igényel. Kérem a türelmet, köszönöm!

# 📦 Készletnyilvántartó Rendszer | Inventory Management System (JavaFX + MySQL)
(#-inventory-management-system)

Egy modern JavaFX és MySQL alapú készletkezelő alkalmazás, amely raktárak, termékek és eladások kezelését teszi lehetővé kisvállalkozások és szolgáltatók számára.

---

## 🎯 Funkciók

- 🏬 Raktárak létrehozása, módosítása, törlése, keresése
- 📦 Termékek hozzáadása, módosítása, törlése, képfeltöltéssel
- 🛒 Termékek kosárba helyezése és eladáskezelés
- 🖨 PDF tranzakció jelentések generálása
- 🔍 Valós idejű keresés táblázatokban
- 💾 Perzisztens adatmentés MySQL adatbázisba

---

## ⚙️ Technológiák

| Komponens | Leírás |
|----------|--------|
| JavaFX | Grafikus felhasználói felület (FXML, SceneBuilder, CSS) |
| MySQL | Perzisztens adatkezelés adatbázisban |
| JDBC | SQL műveletek végrehajtása Java-ból |
| MVC | Modell-Nézet-Vezérlő architektúra |
| SSADM | Strukturált rendszer-analízis és tervezés módszertan |

---

## 🧩 Főbb Működési Logika

### Osztályok:

- `Inventory`, `Product`, `BasketProduct`, `Transaction` — üzleti logika objektumok
- `DatabaseManager` — adatbázis műveletek kezelése

### Fontosabb Függvények:

- `CreateInventoryGraphic()`, `CreateNewProduct()` — új raktár és termék grafikai megjelenítése
- `AddToBasket()`, `RemoveFromBasket()` — kosár kezelés
- `FinalizeTransaction()`, `PrintReport()` — tranzakciók rögzítése, PDF nyomtatás

---

## 🚀 További Fejlesztési Lehetőségek

- 📊 Globális bevételi összesítő statisztika
- 🔐 Jogosultságkezelés (szuperuser / általános user)

---

# 📦 Inventory Management System (JavaFX + MySQL)
(#-inventory-management-system)

A modern inventory management application built with JavaFX and MySQL, designed for small businesses and service providers to manage warehouses, products, and sales.

---

## 🎯 Features

- 🏬 Create, update, delete, and search warehouses
- 📦 Manage products with image uploads
- 🛒 Add products to basket and handle sales
- 🖨 Generate PDF transaction reports
- 🔍 Real-time table search
- 💾 Persistent storage with MySQL database

---

## ⚙️ Technologies

| Component | Description |
|----------|-------------|
| JavaFX | GUI development (FXML, SceneBuilder, CSS) |
| MySQL | Database management |
| JDBC | SQL operations from Java |
| MVC | Model-View-Controller architecture |
| SSADM | Structured Systems Analysis and Design Methodology |

---

## 🧩 Main Working Logic

### Classes:

- `Inventory`, `Product`, `BasketProduct`, `Transaction` — business logic objects
- `DatabaseManager` — database operation handler

### Key Functions:

- `CreateInventoryGraphic()`, `CreateNewProduct()` — create GUI elements for inventory and product
- `AddToBasket()`, `RemoveFromBasket()` — basket management
- `FinalizeTransaction()`, `PrintReport()` — finalizing transactions and printing PDF reports

---


## 🚀 Future Development Possibilities

- 📊 Global revenue summary statistics
- 🔐 User access management (superuser / general user)
