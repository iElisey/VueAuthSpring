<template>
  <v-container>
    <v-card>
      <v-card-title><b>{{ currentUser.username }}&nbsp;</b> Profile</v-card-title>
      <v-card-text>
        <p>
          <v-img :src="'https://skin.vimeworld.com/head/'+currentUser.username+'.png'" :width="100" :height="100">
          </v-img>
        </p>
        <p>
          <strong>Id:</strong>
          {{ currentUser.id }}
        </p>
        <strong>Authorities:</strong>
        <ul v-if="currentUser.authorities != null">
          <li v-for="(role,index) in currentUser.authorities" :key="index">{{ role.authority }}</li>
        </ul>
        <ul v-else>
          <li v-for="(role,index) in currentUser.roles" :key="index">{{ role }}</li>
        </ul>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Profile',
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    }
  },
  mounted() {
    if (!this.currentUser) {
      this.$router.push('/login');
    }
  }
};
</script>