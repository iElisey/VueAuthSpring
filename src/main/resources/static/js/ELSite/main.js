import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import Toast from "vue-toastification";
import "vue-toastification/dist/index.css";
import Vuex from 'vuex';
import {router} from './router';
import store from "./store/index";

Vue.config.productionTip = false

Vue.use(Vuex);
Vue.use(Toast, {
    transition: "Vue-Toastification__bounce",
    maxToasts: 20,
    newestOnTop: true
});

var app = new Vue({
    router,
    vuetify,
    store,
    el: '#app',
    render: h => h(App),
});
