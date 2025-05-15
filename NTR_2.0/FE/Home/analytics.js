function inizializzaGrafici() {
  const pieData = {
    labels: ["Affitto", "Spesa", "Trasporti", "Svago", "Altro"],
    datasets: [{
      data: [500, 300, 100, 80, 120],
      backgroundColor: ["#0077b6", "#00b4d8", "#90e0ef", "#caf0f8", "#ade8f4"]
    }]
  };

  const lineData = {
    labels: ["Gen", "Feb", "Mar", "Apr", "Mag", "Giu"],
    datasets: [{
      label: "Totale Spese €",
      data: [800, 950, 720, 870, 940, 990],
      fill: false,
      borderColor: "#0077b6",
      tension: 0.3
    }]
  };

  const barData = {
    labels: ["Gen", "Feb", "Mar", "Apr", "Mag", "Giu"],
    datasets: [
      {
        label: "Entrate",
        data: [1200, 1100, 1300, 1250, 1400, 1350],
        backgroundColor: "#0077b6"
      },
      {
        label: "Uscite",
        data: [800, 950, 720, 870, 940, 990],
        backgroundColor: "#90e0ef"
      }
    ]
  };

  new Chart(document.getElementById("pieChart"), {
    type: "pie",
    data: pieData
  });

  new Chart(document.getElementById("lineChart"), {
    type: "line",
    data: lineData
  });

  new Chart(document.getElementById("barChart"), {
    type: "bar",
    data: barData,
    options: {
      responsive: true,
      scales: {
        y: {
          beginAtZero: true
        }
      }
    }
  });
}