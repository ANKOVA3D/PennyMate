async function loadProfile() {
  try {
    const response = await fetch("http://localhost:8080/users/me", {
      credentials: "include"
    });

    if (!response.ok) {
      throw new Error("Errore nel caricamento del profilo");
    }

    const userData = await response.json();

    // Popola i campi del form
    document.getElementById("userId").value = userData.id || "";
    document.getElementById("name").value = userData.username || "";
    document.getElementById("email").value = userData.email || "";

    // Aggiunge listener solo una volta
    const form = document.getElementById("editProfileForm");
    if (!form.dataset.listenerAdded) {
      form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const updates = {
          id: document.getElementById("userId").value,
          username: document.getElementById("name").value.trim(),
          email: document.getElementById("email").value.trim(),
          password: document.getElementById("password").value
        };

        try {
          const updateResponse = await fetch("http://localhost:8080/users/update", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            credentials: "include",
            body: JSON.stringify(updates)
          });

          const result = await updateResponse.json();

          if (updateResponse.ok) {
            alert("Profilo aggiornato con successo!");
            window.location.href = "index.html";
			me();
          } else {
            throw new Error(result.message || "Errore nell'aggiornamento");
          }
        } catch (error) {
          console.error("Errore durante l'aggiornamento:", error);
          alert(error.message);
        }
      });

      form.dataset.listenerAdded = "true";
    }
  } catch (error) {
    console.error("Errore nel caricamento iniziale:", error);
    alert("Impossibile caricare i dati del profilo");
    window.location.href = "index.html";
  }
}
