document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("editTransactionForm");
  const id = new URLSearchParams(window.location.search).get("id");

  // Carica categorie
  fetch("/api/categories") // adatta questo endpoint se diverso
    .then(res => res.json())
    .then(data => {
      const categorySelect = document.getElementById("category");
      data.forEach(cat => {
        const opt = document.createElement("option");
        opt.value = cat.id;
        opt.textContent = cat.name;
        categorySelect.appendChild(opt);
      });
    });

  // Carica dati transazione
  fetch(`/api/transactions/${id}`)
    .then(res => res.json())
    .then(t => {
      document.getElementById("transactionId").value = t.id;
      document.getElementById("description").value = t.description;
      document.getElementById("amount").value = t.amount;
      document.getElementById("category").value = t.categoryId;
    });

  // Submit
  form.addEventListener("submit", (e) => {
    e.preventDefault();

    const t = {
      id: document.getElementById("transactionId").value,
      description: document.getElementById("description").value,
      amount: document.getElementById("amount").value,
      categoryId: document.getElementById("category").value
    };

    fetch("/api/transactions/update", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(t)
    }).then(res => {
      if (res.ok) {
        alert("Transazione aggiornata!");
        window.location.href = "index.html";
      } else {
        alert("Errore durante l'aggiornamento");
      }
    });
  });
});
