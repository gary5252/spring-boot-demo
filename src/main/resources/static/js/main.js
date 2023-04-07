
(function ($) {
    "use strict";


    /*==================================================================
    [ Validate ]*/
    var input = $('.validate-input .input100');

    $('.validate-form').on('submit', function () {
        var check = true;

        for (var i = 0; i < input.length; i++) {
            if (validate(input[i]) == false) {
                showValidate(input[i]);
                check = false;
            }
        }

        return check;
    });


    $('.validate-form .input100').each(function () {
        $(this).focus(function () {
            hideValidate(this);
        });
    });

    function validate(input) {
        if ($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
            if ($(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
                return false;
            }
        }
        else {
            if ($(input).val().trim() == '') {
                return false;
            }
        }
    }

    function showValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).removeClass('alert-validate');
    }



})(jQuery);


var checkEye = document.getElementById("checkEye");
var floatingPassword = document.getElementById("floatingPassword");
checkEye.addEventListener("click", function (e) {
    if (e.target.classList.contains('fa-eye')) {
        //換class 變換 type
        e.target.classList.remove('fa-eye');
        e.target.classList.add('fa-eye-slash');
        floatingPassword.setAttribute('type', 'text')
    } else {
        floatingPassword.setAttribute('type', 'password');
        e.target.classList.remove('fa-eye-slash');
        e.target.classList.add('fa-eye')
    }
});


var checkEye2 = document.getElementById("checkEye2");
var floatingPassword2 = document.getElementById("floatingPassword2");
checkEye2.addEventListener("click", function (e) {
    if (e.target.classList.contains('fa-eye')) {
        //換class 變換 type
        e.target.classList.remove('fa-eye');
        e.target.classList.add('fa-eye-slash');
        floatingPassword2.setAttribute('type', 'text')
    } else {
        floatingPassword2.setAttribute('type', 'password');
        e.target.classList.remove('fa-eye-slash');
        e.target.classList.add('fa-eye')
    }
});

var checkEye3 = document.getElementById("checkEye3");
var floatingPassword3 = document.getElementById("floatingPassword3");
checkEye3.addEventListener("click", function (e) {
    if (e.target.classList.contains('fa-eye')) {
        //換class 變換 type
        e.target.classList.remove('fa-eye');
        e.target.classList.add('fa-eye-slash');
        floatingPassword3.setAttribute('type', 'text')
    } else {
        floatingPassword3.setAttribute('type', 'password');
        e.target.classList.remove('fa-eye-slash');
        e.target.classList.add('fa-eye')
    }
});


// =================================================================
// CountDown Timer
// let countdown;
// const timerDisplay = document.querySelector('.display__time-left');
// const endTime = document.querySelector('.display__end-time');
// const buttons = document.querySelectorAll('[data-time]');

// window.onload = function () {
//     // const times = document.customForm.seconds.value;
//     const times = document.getElementById("seconds").value;
//     timer(times);
// }

// function timer(seconds) {
//     // clear any existing timers
//     clearInterval(countdown);

//     const now = Date.now();
//     const then = now + seconds * 1000;
//     displayTimeLeft(seconds);
//     displayEndTime(then);

//     countdown = setInterval(() => {
//         const secondsLeft = Math.round((then - Date.now()) / 1000);
//         // const secondsLeft = seconds--;
//         // check if we should stop it!
//         if (secondsLeft < 0) {
//             clearInterval(countdown);
//             // alert("Time Over!");
//             document.title = 'Time Over!';
//             timerDisplay.textContent = 'Time Over!';
//             return;
//         }
//         // display it
//         displayTimeLeft(secondsLeft);
//     }, 1000);
// }

// function displayTimeLeft(seconds) {
//     const hours = Math.floor(seconds / 3600);
//     const minutes = Math.floor((seconds % 3600) / 60);
//     const remainderSeconds = seconds % 60;
//     const display = `${hours < 10 ? '0' : ''}${hours}:${minutes < 10 ? '0' : ''}${minutes}:${remainderSeconds < 10 ? '0' : ''}${remainderSeconds}`;
//     document.title = display;
//     timerDisplay.textContent = display;
// }

// function displayEndTime(timestamp) {
//     const end = new Date(timestamp);
//     const hour = end.getHours();
//     const adjustedHour = hour > 12 ? hour - 12 : hour;
//     const minutes = end.getMinutes();
//     endTime.textContent = `Be Back At ${adjustedHour}:${minutes < 10 ? '0' : ''}${minutes}`;
// }

// function startTimer() {
//     const seconds = parseInt(this.dataset.time);
//     timer(seconds);
// }

// buttons.forEach(button => button.addEventListener('click', startTimer));
// document.customForm.addEventListener('submit', function (e) {
//     e.preventDefault();
//     const secs = this.seconds.value;
//     console.log(secs);
//     // timer(mins * 60);
//     timer(secs);
//     this.reset();
// });

// =================================================================

