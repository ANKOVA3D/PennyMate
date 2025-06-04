

    function renderDeleteButton() {
	  const deleteFormSection = document.getElementById('deleteProfileForm');
      deleteFormSection.innerHTML = `
        <button id="buttonDeleteProfile" type="button" style="margin-top: 0px;">Elimina profilo</button>
      `;
		document.getElementById('buttonDeleteProfile').addEventListener('click', function() {
		handleDeleteClick(deleteFormSection);
		});
    }

    function handleDeleteClick(deleteFormSection) {
  deleteFormSection.innerHTML = `
    <div style="display: flex; flex-direction: column; align-items: center;">
      <p style="margin-bottom: 10px;">Sei sicuro di voler eliminare il tuo profilo?</p>
      <div class="confirmation-buttons">
        <button id="confirmCancel" class="btn-secondary">Annulla</button>
        <button id="confirmDelete" style="background-color: red; color: white;">Conferma</button>
      </div>
    </div>
  `;

  document.getElementById('confirmCancel').addEventListener('click', renderDeleteButton);

  document.getElementById('confirmDelete').addEventListener('click', function () {
    fetch('http://localhost:8080/users/delete', {
      method: 'DELETE',
      credentials: 'include' // per mandare i cookie/sessione
    })
      .then(response => {
        if (response.ok) {
          alert("Profilo eliminato con successo.");
          window.location.href = '/'; // oppure a una pagina di logout
        } else if (response.status === 401) {
          alert("Non sei autenticato.");
        } else {
          return response.json().then(data => {
            throw new Error(data.message || 'Errore nella cancellazione.');
          });
        }
      })
      .catch(error => {
        console.error('Errore:', error);
        alert("Si è verificato un errore: " + error.message);
      });
  });
}


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
