
document.addEventListener('DOMContentLoaded', function () {
               const calendarEl = document.getElementById('calendar');
        let calendar;

        // Attach event listeners to buttons that toggle the modal
        document.querySelectorAll('[data-bs-toggle="modal"]').forEach(button => {
            button.addEventListener('click', function () {
                const studentId = this.getAttribute('data-student-id');
                populateCalendar(studentId);
            });
        });

function populateCalendar(studentId) {
    const calendarEl = document.getElementById('calendar');
    const modal = document.getElementById('attendanceModal');
    const studentAttendanceDates = JSON.parse(modal.getAttribute('data-attendance-dates'));

    // Safely get attendance dates, default to an empty array if undefined
    let attendanceDates = studentAttendanceDates[studentId] || [];

    // Log the attendance dates for debugging
    console.log('Attendance Dates for Student ID:', studentId, attendanceDates);

    // Ensure attendanceDates is an array
    if (!Array.isArray(attendanceDates)) {
        console.error('Attendance dates is not an array:', attendanceDates);
        attendanceDates = []; // Default to an empty array
    }

    // Clear previous calendar content
    if (calendar) {
        calendar.destroy(); // Destroy the previous calendar instance
    }

    // Clear the calendar element
    calendarEl.innerHTML = '';

    // Prepare events for FullCalendar
    const events = attendanceDates.map(date => ({
        title: 'Present',
        start: date, // Date in YYYY-MM-DD format
        allDay: true,
        color: 'green' // Set the event color to green
    }));

    // Log the events to ensure they're formatted correctly
    console.log('Formatted Events:', events);
    console.log('Is array:', Array.isArray(events)); // Should be true

    // Initialize FullCalendar
    calendar = new FullCalendar.Calendar(calendarEl, {
        height: 400,
        aspectRatio: 2,
        initialView: 'dayGridMonth',
        events: events, // Use the prepared events array
        dateClick: function(info) {
            alert('Clicked on: ' + info.dateStr);
        }
    });

    // Render the calendar
    calendar.render();
}
        // Ensure the calendar is reset properly when the modal is hidden
        const attendanceModal = document.getElementById('attendanceModal');
        attendanceModal.addEventListener('hidden.bs.modal', function () {
            if (calendar) {
                calendar.destroy();
                calendar = null;
            }
            document.getElementById('calendar').innerHTML = '';
        });
    });