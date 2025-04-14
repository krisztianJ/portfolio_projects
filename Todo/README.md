FOR ENGLISH VERSION SCROLL DOWN

# ✅ ToDo Lista (React, SCSS)

Egy letisztult, mégis funkciókban gazdag feladatkezelő alkalmazás **React** felhasználásával.  
Támogatja a feladatok létrehozását, szerkesztését, szűrését, állapotkezelését és megjelenítését világos és sötét témában is.

---

## 🎯 Funkciók

- ✅ Új feladatok hozzáadása névvel, prioritással és határidővel  
- 📝 Feladatok szerkesztése, törlése, állapotváltása (kész / aktív)  
- 📅 Határidő megadása és lejárati figyelmeztetés  
- 📊 Keresés szöveg alapján és szűrés státusz szerint (`minden`, `kész`, `aktív`)  
- 🎨 Prioritáshoz tartozó színkódok (piros, sárga, zöld)  
- 🌙 Témaváltás világos / sötét mód között  
- 💾 Automatikus mentés a localStorage-be  
- 📱 Mobilbarát, reszponzív kialakítás  

---

## 🛠️ Felhasznált technológiák

| Komponens     | Leírás                                |
|---------------|----------------------------------------|
| `React`       | Komponens-alapú UI és állapotkezelés   |
| `useState`    | Dinamikus felhasználói állapot tárolás |
| `useEffect`   | Életcikluskezelés, localStorage szinkronizálás |
| `SCSS`        | Testreszabott stílus világos/sötét módhoz |
| `localStorage`| Állapot mentése és visszatöltése       |

---

## 🧪 Működési logika

### Új feladat hozzáadása:
```jsx
const handleAdd = () => {
  if (newTask.trim() === "") return;
  const newItem = {
    text: newTask.trim(),
    priority,
    dueDate,
    completed: false,
    createdAt: Date.now()
  };
  setTasks([...tasks, newItem]);
  setNewTask("");
  setPriority("medium");
  setDueDate("");
};
```

### Feladat szűrése és keresése:
```js
const filteredTasks = tasks.filter(task => {
  const matchFilter =
    filter === "all" ||
    (filter === "completed" && task.completed) ||
    (filter === "active" && !task.completed);

  const matchSearch = task.text.toLowerCase().includes(searchText.toLowerCase());
  return matchFilter && matchSearch;
});
```

---

# ✅ ToDo List (React, SCSS)

A sleek and feature-rich task management application built using **React**.  
It supports task creation, editing, filtering, state handling, and theming with light/dark modes.

---

## 🎯 Features

- ✅ Add tasks with text, priority, and due date  
- 📝 Edit, delete and toggle task completion  
- 📅 Deadline input and overdue warnings  
- 📊 Search by keyword and filter by status (`all`, `completed`, `active`)  
- 🎨 Color-coded priorities (red, yellow, green)  
- 🌙 Toggle light / dark mode  
- 💾 Auto-save tasks using localStorage  
- 📱 Fully responsive on mobile devices  

---

## 🛠️ Technologies Used

| Component     | Description                            |
|---------------|----------------------------------------|
| `React`       | Component-based UI and state handling  |
| `useState`    | Dynamic state management               |
| `useEffect`   | Lifecycle handling, localStorage sync  |
| `SCSS`        | Custom styling with light/dark support |
| `localStorage`| Persistent state saving and loading    |

---

## 🧪 Logic Overview

### Add new task:
```jsx
const handleAdd = () => {
  if (newTask.trim() === "") return;
  const newItem = {
    text: newTask.trim(),
    priority,
    dueDate,
    completed: false,
    createdAt: Date.now()
  };
  setTasks([...tasks, newItem]);
  setNewTask("");
  setPriority("medium");
  setDueDate("");
};
```

### Task filtering and searching:
```js
const filteredTasks = tasks.filter(task => {
  const matchFilter =
    filter === "all" ||
    (filter === "completed" && task.completed) ||
    (filter === "active" && !task.completed);

  const matchSearch = task.text.toLowerCase().includes(searchText.toLowerCase());
  return matchFilter && matchSearch;
});
```

---


