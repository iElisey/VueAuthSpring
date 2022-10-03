export const snackbar = {
    namespaced: true,
    state:{
        snackbar: false,
        err: '',
    },
    mutations: {
        updateSnackbar (state, value) {
            state.snackbar=value;
        },
        updateError(state, err) {
            state.err=err;
        },
    }
};

