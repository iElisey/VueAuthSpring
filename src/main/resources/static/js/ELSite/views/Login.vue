<template>
  <div>
    <v-container>
      <v-card>
        <v-card-title>Authorization</v-card-title>
        <v-card-text>
          <v-text-field @keydown.enter="handleLogin" v-model="user.token" :loading="loading"
                        label="Enter token"></v-text-field>
          <small>Enter on the VimeWorld command <b>/api auth</b></small>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="handleLogin" color="primary" :loading="loading">LOGIN</v-btn>
        </v-card-actions>
        <v-card-text>
          <v-alert v-if="message" type="error"> {{ message }}</v-alert>
        </v-card-text>
      </v-card>
    </v-container>
  </div>
</template>

<script>
import User from "../models/user";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Login",
  data() {
    return {
      loading: false,
      user: new User('', ''),
      message: ''

    }
  },
  computed: {
    loggedIn() {
      return this.$store.state.auth.status.loggedIn;
    }
  },
  created() {
    if (this.loggedIn) {
      this.$router.push('/profile');
    }
  },
  methods: {
    handleLogin() {

      if (this.user.token) {
        this.loading = true;
        this.$store.dispatch('auth/login', this.user).then(
            () => {
              this.$router.push('/profile');
              this.loading = false;
              this.message = '';
            },
            error => {
              this.loading = false;
              this.message =
                  (error.response && error.response.data && error.response.data.message) ||
                  error.message ||
                  error.toString();
            }
        );
      }
    }
  }
}
</script>

<style scoped>

</style>