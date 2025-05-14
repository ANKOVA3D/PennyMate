
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("editProfileForm");


  form.addEventListener("submit", (e) => {
    e.preventDefault();

    const updatedUser = {
      name: document.getElementById("name").value,
      email: document.getElementById("email").value,
      password: document.getElementById("password").value
    };

    fetch("http://localhost:8080/users/editprofile", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
	  credentials: "include",
      body: JSON.stringify(updatedUser)
    }).then(res => {
      if (res.ok) {
		me();
        alert("Profilo aggiornato!");
        window.location.href = "index.html";
      } else {
        alert("Errore nell'aggiornamento");
      }
    });
  });
});
