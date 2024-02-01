

const userTheme = localStorage.getItem('theme')
let status = false

const btn__dark = document.querySelector('.__dark')
const btn__light = document.querySelector('.__light')

if(userTheme == 'dark') {
	btn__dark.style.display = "none"
    btn__light.style.display = ""
} else if(userTheme == 'light') {
	btn__dark.style.display = ""
    btn__light.style.display = "none"
} else {
	document.documentElement.setAttribute('data-theme', 'light')
	btn__dark.style.display = ""
    btn__light.style.display = "none"
}

document.addEventListener('DOMContentLoaded', () => {
    if (userTheme === 'dark') {
        clickDarkMode()
    } else if (userTheme === 'light') {
        clickLightMode()
    }
})

// 버튼클릭
btn__dark.addEventListener('click', () => {
    if (status === false) {
        clickDarkMode()
    } else if (status === true) {
        clickLightMode()
    }
})

// 버튼클릭
btn__light.addEventListener('click', () => {
    if (status === false) {
        clickDarkMode()
    } else if (status === true) {
        clickLightMode()
    }
})

// 다크/라이트 전환이벤트
function clickDarkMode() {
    localStorage.setItem("theme", "dark")
    document.documentElement.setAttribute('data-theme', 'dark')
    document.body.dataset.theme = 'dark'
    btn__dark.style.display = "none"
    btn__light.style.display = ""
    status = true
}

function clickLightMode() {
    localStorage.setItem("theme", "light")
    document.documentElement.setAttribute('data-theme', 'light')
    document.body.dataset.theme = 'light'
    btn__dark.style.display = ""
    btn__light.style.display = "none"
    status = false
}