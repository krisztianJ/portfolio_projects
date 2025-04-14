
import React, { useEffect, useState } from "react";
import "./App.css";

function Todolist() {
  /*React useState hookok 
    Állapot kezelések
    Biztosítják az adatokat különféle komponenseknek
  */
  const [tasks, setTasks] = useState([]); /* Feladatok listája (tömb) és függvény amivel frissítjük 
                                          Kezdőérték üres tömb*/
  const [initialized, setInitialized] = useState(false); /*localStoragehez*/
  const [newTask, setNewTask] = useState("");/*Új feladat szövege és input mező frissítése */
  const [priority, setPriority] = useState("medium"); /*Priorítás beállítása, kezdő érték közepes */
  const [dueDate, setDueDate] = useState("");/*Határidő beállítása, alapból üres érték */
  const [editIndex, setEditIndex] = useState(null); /*A szerkesztés alatt lévő aktuális adatok */
  const [editData, setEditData] = useState({ text: "", priority: "medium", dueDate: "" });
  const [filter, setFilter] = useState(() => localStorage.getItem("filter") || "all");
  const [darkMode, setDarkMode] = useState(() => localStorage.getItem("theme") === "dark");
  const [searchText, setSearchText] = useState("");

  /*
  Életciklus események kezelése (useEffect)
  */
  useEffect(() => { /*Betölti a localStorage-ben tárolt adatokat és menti is zokat változás esetén */
    try {
      const saved = localStorage.getItem("tasks");/*Megpróbáljuk beolvasni a localStorage-ból */
      if (saved) { /*Ha találunk, nem üres vagy nem null értéket */
        const parsed = JSON.parse(saved);/*Javascript objektumá alakítjuk */
        if (Array.isArray(parsed)) { /*Megnézük, hogy tömb-e */
          setTasks(parsed);/*setTask segítségével beállítjuk az állapotot */
        }
      }
    } catch (e) {
      console.error("Hibás JSON a localStorage-ben:", e);/*Hibás formátum esetén */
    }
    setInitialized(true);
  }, []);

  /*Feladatok mentése, módosítás esetén*/
  useEffect(() => {
    if (initialized) {/*Megnézi, hogy a inicializálva van-e a tömb, hogy ne írjük felül óres tömbbel*/
      localStorage.setItem("tasks", JSON.stringify(tasks));/*Ha igen akkor betöltjük a tasks jelenlegi
                                                              állapotát a localStorage-be */
    }
  }, [tasks, initialized]);

  /*Téma beállítása és mentése*/
  useEffect(() => {
    document.body.className = darkMode ? "dark" : ""; /*A dokumentum body elemének classnName tulajdonságát
                                                        állítjuk */
    localStorage.setItem("theme", darkMode ? "dark" : "light");
  }, [darkMode]);

  /*Szűrési beállítás mentése*/
  useEffect(() => { /*Akkor fut le ha a filter állapota megváltozik (all, completed, active) */
    localStorage.setItem("filter", filter);
  }, [filter]);

  /*Új feladat hozzáadása*/
  const handleAdd = () => {
    if (newTask.trim() === "") return;/**Ellenörzi, hogy nem üres és a sorközöket is levágjuk */
    const newItem = { /*Új javascript objektum */
      text: newTask.trim(), /*A feladat szövege a newTask állapotból */
      priority,
      dueDate,
      completed: false,
      createdAt: Date.now()
    };
    setTasks([...tasks, newItem]); /*Hozzáadja a feladatokhoz az új feladatot */
    setNewTask(""); /*Mezők alaphelyzetbe állítása */
    setPriority("medium");
    setDueDate("");
  };

  /*Feladat törlése index alapján */
  const handleDelete = (i) => setTasks(tasks.filter((_, idx) => idx !== i));
  /*Végig megy a task tömbön és törli az i indexű elemet */

  /*Megváltoztatja a completed mező értékét */
  const handleToggleComplete = (i) => { /*Az i indexű feladatot kiválasztjuk */
    const updated = [...tasks]; /*Lemásoljuk a completed mező értékét */
    updated[i].completed = !updated[i].completed; /*Vesszük az érték ellentetjét */
    setTasks(updated); /*Majd ezzel az értékkel frissítjük az állapotot */
  };

  /*Feladat szerkesztése */
  const handleEdit = (i) => {
    setEditIndex(i);
    setEditData(tasks[i]);
  };

  /*Feladat szerkesztésének a mentése */
  const saveEdit = () => {
    const updated = [...tasks]; /*Lemásolja a task tömböt
                                (the law of immutability) */
    updated[editIndex] = editData; /*A kiválasztottat felülírja */
    setTasks(updated);/*Frissíti a listát az új frissített feladattal */
    setEditIndex(null);/*Szerkesztési mód vége -> visszatérés a normál UI módba */
  };

  /*Feladat mozgatása a listában */
  const move = (i, dir) => { /*Mozgatandó feladat aktuális indexe, és a direction(irány) */
    const target = i + dir;/*Kiszámolja a cél indexet ahová mozgatni szeretnénk */
    if (target < 0 || target >= tasks.length) return;/*Ha a lista elején vagy végén van eleve akkor onnan nem tudjuk
                                                      Lejebb vagy feljebb mozgatni*/
    const updated = [...tasks]; /*Lemásoljuk a mostani feladatokat */
    [updated[i], updated[target]] = [updated[target], updated[i]]; /*Kicseréljük a feladatok helyét */
    setTasks(updated); /*Frissítjük az állapotot */
  };

  /*Priorításhoz szín rendelése */
  const getPriorityColor = (level) => ({
    high: "#f87171", /*Piros */
    medium: "#facc15", /*Sárga */
    low: "#34d399" /*Zöld */
  }[level] || "#d1d5db"); /*Szürke, ha nem a három priorítási sorrend közül kerül be */

  /*Szűrés és keresés */
  const filteredTasks = tasks.filter(task => {
    /*Kétféle szűrő a task tömbre alkalmazva */
    const matchFilter =
      filter === "all" ||/*Minden feladat kivan választva */
      (filter === "completed" && task.completed) ||/*completed csak a kész */
      (filter === "active" && !task.completed); /*ha az active akkor csak a még el nem készülteket */

    const matchSearch = task.text.toLowerCase().includes(searchText.toLowerCase());/*Kisbetüssé teszi
                                                                        a szöveg keresés érdekében */
    return matchFilter && matchSearch;
  });

  return (
    <div className="todo-container">
      <div className="header">
        <h2>📝 To-Do Lista</h2>
        <button className="theme-toggle" onClick={() => setDarkMode(!darkMode)} title="Téma váltása">
          {darkMode ? "☀️" : "🌙"}
        </button>
      </div>

      <div className="input-group">
        <input
          type="text"
          value={newTask}
          onChange={(e) => setNewTask(e.target.value)}
          placeholder="Új feladat"
        />
        <select value={priority} onChange={(e) => setPriority(e.target.value)}>
          <option value="low">Alacsony</option>
          <option value="medium">Közepes</option>
          <option value="high">Magas</option>
        </select>
        <input
          type="date"
          value={dueDate}
          onChange={(e) => setDueDate(e.target.value)}
        />
        
        <button onClick={handleAdd} title="Feladat hozzáadása">Hozzáadás</button>
      </div>

        
      <div className="filter-group">
        <label>Szűrés:</label>
        <select value={filter} onChange={(e) => setFilter(e.target.value)}>
          <option value="all">Minden</option>
          <option value="completed">Kész</option>
          <option value="active">Aktív</option>
        </select>

        <input
          type="text"
          placeholder="Keresés..."
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          title="Szöveg alapján szűrés"
        />
      </div>

      <ul className="task-list">
        {filteredTasks.map((task, i) => ( /**Végig megyünk a tömbön */
          <li key={i} /*A kulcs a React-nak kell, hogy nyomon tudja követni */
              className="fade-in"
              style={{ borderLeft: `8px solid ${getPriorityColor(task.priority)}` }}
          >
            {editIndex === i ? (
              <>
                <input
                  value={editData.text}
                  onChange={(e) => setEditData({ ...editData, text: e.target.value })}
                />
                <select
                  value={editData.priority}
                  onChange={(e) => setEditData({ ...editData, priority: e.target.value })}
                >
                  <option value="low">Alacsony</option>
                  <option value="medium">Közepes</option>
                  <option value="high">Magas</option>
                </select>
                <input
                  type="date"
                  value={editData.dueDate}
                  onChange={(e) => setEditData({ ...editData, dueDate: e.target.value })}
                />
                <button className="save-button" onClick={saveEdit} title="Szerkesztés mentése">Mentés</button>
              </>
            ) : (
              <>
                <div className="task-content">
                  <span
                    className="task-text"
                    /*Áthuzza a kész fealadatot */
                    style={{ textDecoration: task.completed ? "line-through" : "none" }}
                  >
                    {task.text}
                  </span>
                  {task.dueDate && (
                    <span className="task-date">
                      📅 {task.dueDate}
                      {new Date(task.dueDate) < new Date().setHours(0,0,0,0) && !task.completed && (
                        <span className="overdue"> ⛔ Lejárt!</span>
                      )}
                    </span>
                  )}
                </div>
                <div className="task-actions">
                  <button onClick={() => handleToggleComplete(i)} title="Kész / Nem kész">
                    {task.completed ? "✅" : "☑️"}
                  </button>
                  <button onClick={() => handleEdit(i)} title="Szerkesztés">✏️</button>
                  <button onClick={() => handleDelete(i)} title="Törlés">🗑️</button>
                  <button onClick={() => move(i, -1)} title="Feljebb mozgatás">⬆️</button>
                  <button onClick={() => move(i, 1)} title="Lejjebb mozgatás">⬇️</button>
                </div>
              </>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}
export default Todolist;
