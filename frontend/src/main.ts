import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { vividVue } from '@vonage/vivid-vue'

import App from './App.vue'
import router from './router'
import { store } from './stores'

const app = createApp(App)
	.use(vividVue, {
		font: 'spezia',
		customComponentPrefix: 'my-app',
	})


app.use(createPinia())
app.use(store)
app.use(router)

app.mount('#app')
