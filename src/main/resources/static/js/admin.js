//    function removeRow(button) {
//        // Get the parent tr element
//        const row = button.parentNode.parentNode.parentNode;
//        // Remove the row from the DOM
//        row.parentNode.removeChild(row);
//    }

    function removeRow(button) {
      // Get the form element
      const form = button.closest('form');

      // Submit the form
      form.submit();

      // After a short delay, remove the row
      setTimeout(() => {
        const row = button.parentNode.parentNode.parentNode;
        row.parentNode.removeChild(row);
      }, 100); // Adjust the delay as needed
    }

          var form1 = document.getElementById("form1");
          var form2 = document.getElementById("form2");
          function submitForm(event) {
             event.preventDefault();
          }
          form1.addEventListener('submit', submitForm);
          form2.addEventListener('submit', submitForm);

console.log("ADMIN JS")

