/* load the page with the right information (all passed in the request) */
function initPage() {

  /* logged in? */
  var rn = String("${adminData}");
  if (rn.length == 0 || rn == "null") {
    window.location = "/housinggame-admin/login";
  }
}

/* handle click on button */
function clickMenu(button) {
  document.getElementById("click").setAttribute("value", button);
  document.getElementById("clickForm").submit();
}

/* handle click on button with record number */
function clickRecordId(button, recordId) {
  document.getElementById("click").setAttribute("value", button);
  document.getElementById("recordNr").setAttribute("value", recordId);
  document.getElementById("clickForm").submit();
}

/* submit edit form */
function submitEditForm(click, recordNr) {
  document.getElementById("editClick").setAttribute("value", click);
  document.getElementById("editRecordNr").setAttribute("value", recordNr);
  document.getElementById("editForm").submit();
}


/* create the preview for the image when a file is selected */
function previewImage(event, imageId) {
  var reader = new FileReader();
  reader.onload = function() {
    var output = document.getElementById(imageId);
    output.src = reader.result;
  }
  reader.readAsDataURL(event.target.files[0]);
  var image_reset = document.getElementById(imageId + "_reset");
  image_reset.value = 'normal';
}

function resetImage(imageId) {
  var image = document.getElementById(imageId);
  image.src = '';
  image.value = '';
  var image_reset = document.getElementById(imageId + "_reset");
  image_reset.value = 'delete';
}


/* Make the modal window div element draggable: */
function dragElement(elmnt) {
  var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
  if (document.getElementById(elmnt.id + "header")) {
    // if present, the header is where you move the DIV from:
    document.getElementById(elmnt.id + "header").onmousedown = dragMouseDown;
  } else {
    // otherwise, move the DIV from anywhere inside the DIV:
    elmnt.onmousedown = dragMouseDown;
  }

  function dragMouseDown(e) {
    e = e || window.event;
    e.preventDefault();
    // get the mouse cursor position at startup:
    pos3 = e.clientX;
    pos4 = e.clientY;
    document.onmouseup = closeDragElement;
    // call a function whenever the cursor moves:
    document.onmousemove = elementDrag;
  }

  function elementDrag(e) {
    e = e || window.event;
    e.preventDefault();
    // calculate the new cursor position:
    pos1 = pos3 - e.clientX;
    pos2 = pos4 - e.clientY;
    pos3 = e.clientX;
    pos4 = e.clientY;
    // set the element's new position:
    elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
    elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
  }

  function closeDragElement() {
    // stop moving when mouse button is released:
    document.onmouseup = null;
    document.onmousemove = null;
  }
}

/* Functions for nullable fields (Issue #65) */
function applyNullStyle(field, makeNull) {
  if (!field) return;

  // Toggle the visual class only; do not dispatch events here.
  if (makeNull) field.classList.add('is-null');
  else field.classList.remove('is-null');

  if (!makeNull) return;

  const tag = field.tagName;
  const type = (field.getAttribute('type') || '').toLowerCase();

  if (tag === 'SELECT') {
    const opt = field.querySelector('option[value="null"], option[value=""]');
    if (opt) field.value = opt.value;
    else field.selectedIndex = -1; // no selection
  } else if (tag === 'INPUT' && type === 'file') {
    field.value = ''; // clear chosen file
  } else if (tag === 'TEXTAREA') {
    field.value = '';
  } else if (tag === 'INPUT' &&
    (type === 'text' || type === 'email' || type === 'number' ||
      type === 'date' || type === 'datetime-local' ||
      type === 'tel' || type === 'url' || type === 'search')) {
    field.value = '';
  } else if (tag === 'INPUT' && type === 'checkbox') {
    // 3-state boolean: null means visually grey, unchecked value
    field.checked = false;
  }
}

function nullToggle(base, checkboxEl) {
  const form = checkboxEl.form || document;
  const field = form.elements[base];
  if (!field) return;

  if (checkboxEl.checked) {
    applyNullStyle(field, true);
  } else {
    applyNullStyle(field, false);
  }
}

function fieldEdited(base, fieldEl) {
  const form = fieldEl.form || document;
  const cb = form.elements[base + '-null'];
  if (cb && cb.checked) cb.checked = false;
  applyNullStyle(fieldEl, false);
}

// if some -null boxes are initially checked, sync visuals on load
window.addEventListener('DOMContentLoaded', function() {
  document.querySelectorAll('input[type="checkbox"][name$="-null"]').forEach(function(cb) {
    const base = cb.name.slice(0, -5);
    const form = cb.form || document;
    const field = form.elements[base];
    if (!field) return;
    if (cb.checked) applyNullStyle(field, true);
    else applyNullStyle(field, false); // ensures removal if server prefilled value
  });
});
