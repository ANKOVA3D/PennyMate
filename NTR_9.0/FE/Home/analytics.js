async function inizializzaGrafici() {
  try {
    const [categorie, transazioni] = await Promise.all([
      fetch("http://localhost:8080/category/all", { credentials: "include" }).then(res => res.json()),
      fetch("http://localhost:8080/transaction/all", { credentials: "include" }).then(res => res.json())
    ]);

    // === Spese per categoria (Pie Chart) ===
    const spesePerCategoria = {};
    categorie.forEach(cat => spesePerCategoria[cat] = 0);

    transazioni.forEach(t => {
      if (!t.type && spesePerCategoria.hasOwnProperty(t.category)) {
        spesePerCategoria[t.category] += t.amount;
      }
    });

    const pieData = {
      labels: Object.keys(spesePerCategoria),
      datasets: [{
        data: Object.values(spesePerCategoria),
        backgroundColor: [
          "#0077b6", "#00b4d8", "#90e0ef", "#caf0f8", "#ade8f4",
          "#ffafcc", "#ffc8dd", "#cdb4db", "#bde0fe", "#a2d2ff"
        ]
      }]
    };

    // === Spese ed Entrate Mensili ===
    const speseMensili = Array(12).fill(0);
    const entrateMensili = Array(12).fill(0);

    transazioni.forEach(t => {
      const mese = new Date(t.date).getMonth();
      if (t.type) {
        entrateMensili[mese] += t.amount;
      } else {
        speseMensili[mese] += t.amount;
      }
    });

    const mesi = ["Gen", "Feb", "Mar", "Apr", "Mag", "Giu", "Lug", "Ago", "Set", "Ott", "Nov", "Dic"];

    // === Line Chart: Spese ===
    const lineData = {
      labels: mesi,
      datasets: [{
        label: "Totale Spese €",
        data: speseMensili,
        fill: false,
        borderColor: "#0077b6",
        tension: 0.3
      }]
    };

    // === Bar Chart: Entrate ===
    const barData = {
      labels: mesi,
      datasets: [{
        label: "Entrate Mensili €",
        data: entrateMensili,
        backgroundColor: "#90e0ef"
      }]
    };

    // === Line Chart Comparativa: Entrate vs Uscite ===
    const lineComparisonData = {
      labels: mesi,
      datasets: [
        {
          label: "Entrate",
          data: entrateMensili,
          borderColor: "#38b000",
          fill: false,
          tension: 0.3
        },
        {
          label: "Uscite",
          data: speseMensili,
          borderColor: "#d00000",
          fill: false,
          tension: 0.3
        }
      ]
    };
	
	// === Entrate per categoria (Pie Chart) ===
const entratePerCategoria = {};
categorie.forEach(cat => entratePerCategoria[cat] = 0);

transazioni.forEach(t => {
  if (t.type && entratePerCategoria.hasOwnProperty(t.category)) {
    entratePerCategoria[t.category] += t.amount;
  }
});

const pieEntrateData = {
  labels: Object.keys(entratePerCategoria),
  datasets: [{
    data: Object.values(entratePerCategoria),
    backgroundColor: [
      "#06d6a0", "#118ab2", "#ffd166", "#ef476f", "#073b4c",
      "#caffbf", "#fdffb6", "#bdb2ff", "#ffc6ff", "#9bf6ff"
    ]
  }]
};


    // === Disegna Grafici ===
    new Chart(document.getElementById("pieChart"), {
      type: "pie",
      data: pieData
    });

    new Chart(document.getElementById("lineChart"), {
      type: "line",
      data: lineData
    });

    new Chart(document.getElementById("barChartEntrate"), {
      type: "bar",
      data: barData
    });
	
	new Chart(document.getElementById("pieChartEntrate"), {
	  type: "pie",
	  data: pieEntrateData
	});


    new Chart(document.getElementById("lineChartEntrateUscite"), {
      type: "line",
      data: lineComparisonData
    });

  } catch (err) {
    console.error("Errore nell'inizializzazione dei grafici:", err);
  }
}
