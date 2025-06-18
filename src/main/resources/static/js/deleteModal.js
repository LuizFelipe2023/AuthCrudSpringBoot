let deleteModal = document.getElementById("deleteModal");
deleteModal.addEventListener("show.bs.modal", function (event) {
    let button = event.relatedTarget;
    let url = button.getAttribute("data-delete-url");

    let form = deleteModal.querySelector("#deleteForm");
    form.setAttribute("action", url);
});