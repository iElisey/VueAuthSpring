import Vue from 'vue';
import Vuex from 'vuex';

import {auth} from './auth.module';
Vue.use(Vuex);

export default new Vuex.Store({
    modules: {
        auth
    },
    state:{
        snackbar: false,
        err: '',
        requestCount: 0,
    },
    mutations: {
        updateSnackbar (state, value) {
            state.snackbar=value;
        },
        updateError(state, err) {
            state.err=err;
        },
        updateRequestCount(state, request) {
            state.requestCount += request;
        },
        makeZeroRequestCount(state) {
            state.requestCount = 0;
        },
    }
});
