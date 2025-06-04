// === Variabili globali per riferimento facoltativo (non obbligatorio se usi destroyChartByCanvasId) ===
let pieChartInstance, lineChartInstance, barChartEntrateInstance, pieChartEntrateInstance, lineChartEntrateUsciteInstance;

// === Helper per distruggere chart in sicurezza ===
function destroyChartByCanvasId(canvasId) {
  const chart = Chart.getChart(canvasId);
  if (chart) chart.destroy();
}

async function inizializzaGrafici() {
  try {
    // === Distruggi eventuali istanze precedenti dei grafici ===
    destroyChartByCanvasId("pieChart");
    destroyChartByCanvasId("lineChart");
    destroyChartByCanvasId("barChartEntrate");
    destroyChartByCanvasId("pieChartEntrate");
    destroyChartByCanvasId("lineChartEntrateUscite");

    // === Recupero dati ===
    const [categorie, transazioni] = await Promise.all([
      fetch("http://localhost:8080/category/all", { credentials: "include" }).then(res => {
        if (!res.ok) throw new Error(`HTTP error! status: ${res.status} for categories`);
        return res.json();
      }),
      fetch("http://localhost:8080/transaction/all", { credentials: "include" }).then(res => {
        if (!res.ok) throw new Error(`HTTP error! status: ${res.status} for transactions`);
        return res.json();
      })
    ]);

    // === Spese per categoria ===
    const spesePerCategoria = {};
    if (Array.isArray(categorie)) {
      categorie.forEach(cat => spesePerCategoria[cat] = 0);
    }

    if (Array.isArray(transazioni)) {
      transazioni.forEach(t => {
        if (!t.type && spesePerCategoria.hasOwnProperty(t.category)) {
          spesePerCategoria[t.category] += t.amount;
        }
      });
    }

    const pieData = {
      labels: Object.keys(spesePerCategoria),
      datasets: [{
        data: Object.values(spesePerCategoria),
        backgroundColor: ["#0077b6", "#00b4d8", "#90e0ef", "#caf0f8", "#ade8f4", "#ffafcc", "#ffc8dd", "#cdb4db", "#bde0fe", "#a2d2ff"]
      }]
    };

    // === Spese ed Entrate Mensili ===
    const speseMensili = Array(12).fill(0);
    const entrateMensili = Array(12).fill(0);

    if (Array.isArray(transazioni)) {
      transazioni.forEach(t => {
        const mese = new Date(t.date).getMonth();
        if (mese >= 0 && mese < 12) {
          if (t.type) {
            entrateMensili[mese] += t.amount;
          } else {
            speseMensili[mese] += t.amount;
          }
        }
      });
    }

    const mesi = [
      getTranslation('month_jan'), getTranslation('month_feb'), getTranslation('month_mar'),
      getTranslation('month_apr'), getTranslation('month_may'), getTranslation('month_jun'),
      getTranslation('month_jul'), getTranslation('month_aug'), getTranslation('month_sep'),
      getTranslation('month_oct'), getTranslation('month_nov'), getTranslation('month_dec')
    ];

    const lineData = {
      labels: mesi,
      datasets: [{
        label: getTranslation('analytics_label_total_expenses'),
        data: speseMensili,
        fill: false,
        borderColor: "#0077b6",
        tension: 0.3
      }]
    };

    const barData = {
      labels: mesi,
      datasets: [{
        label: getTranslation('analytics_label_monthly_income'),
        data: entrateMensili,
        backgroundColor: "#90e0ef"
      }]
    };

    const lineComparisonData = {
      labels: mesi,
      datasets: [
        {
          label: getTranslation('analytics_label_income'),
          data: entrateMensili,
          borderColor: "#38b000",
          fill: false,
          tension: 0.3
        },
        {
          label: getTranslation('analytics_label_outgoings'),
          data: speseMensili,
          borderColor: "#d00000",
          fill: false,
          tension: 0.3
        }
      ]
    };

    // === Entrate per categoria ===
    const entratePerCategoria = {};
    if (Array.isArray(categorie)) {
      categorie.forEach(cat => entratePerCategoria[cat] = 0);
    }

    if (Array.isArray(transazioni)) {
      transazioni.forEach(t => {
        if (t.type && entratePerCategoria.hasOwnProperty(t.category)) {
          entratePerCategoria[t.category] += t.amount;
        }
      });
    }

    const pieEntrateData = {
      labels: Object.keys(entratePerCategoria),
      datasets: [{
        data: Object.values(entratePerCategoria),
        backgroundColor: ["#06d6a0", "#118ab2", "#ffd166", "#ef476f", "#073b4c", "#caffbf", "#fdffb6", "#bdb2ff", "#ffc6ff", "#9bf6ff"]
      }]
    };

    // === Creazione grafici se canvas presente ===
    const pieChartCanvas = document.getElementById("pieChart");
    if (pieChartCanvas) {
      pieChartInstance = new Chart(pieChartCanvas, {
        type: "pie",
        data: pieData,
        options: { plugins: { title: { display: false } } }
      });
    }

    const lineChartCanvas = document.getElementById("lineChart");
    if (lineChartCanvas) {
      lineChartInstance = new Chart(lineChartCanvas, {
        type: "line",
        data: lineData,
        options: { plugins: { title: { display: false } } }
      });
    }

    const barChartEntrateCanvas = document.getElementById("barChartEntrate");
    if (barChartEntrateCanvas) {
      barChartEntrateInstance = new Chart(barChartEntrateCanvas, {
        type: "bar",
        data: barData,
        options: { plugins: { title: { display: false } } }
      });
    }

    const pieChartEntrateCanvas = document.getElementById("pieChartEntrate");
    if (pieChartEntrateCanvas) {
      pieChartEntrateInstance = new Chart(pieChartEntrateCanvas, {
        type: "pie",
        data: pieEntrateData,
        options: { plugins: { title: { display: false } } }
      });
    }

    const lineChartEntrateUsciteCanvas = document.getElementById("lineChartEntrateUscite");
    if (lineChartEntrateUsciteCanvas) {
      lineChartEntrateUsciteInstance = new Chart(lineChartEntrateUsciteCanvas, {
        type: "line",
        data: lineComparisonData,
        options: { plugins: { title: { display: false } } }
      });
    }

  } catch (err) {
    console.error(getTranslation('alert_network_or_server_error') + ": " + err.message);
    const chartsContainer = document.getElementById('grafici-container');
    if (chartsContainer) {
      // chartsContainer.innerHTML = `<p>${getTranslation('error_loading_chart_data')}</p>`;
    }
  }
}
