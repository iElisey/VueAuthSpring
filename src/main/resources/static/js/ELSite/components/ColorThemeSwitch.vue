<template>
  <v-tooltip bottom>
    <template #activator="{ on }">
      <v-btn v-on="on" icon @click="switchTheme">
        <v-icon>
          {{ $vuetify.theme.dark ? 'mdi-white-balance-sunny' : 'mdi-moon-waxing-crescent' }}
        </v-icon>
      </v-btn>
    </template>
    <span>Change theme</span>
  </v-tooltip>
</template>

<script>
export default {
  created() {
    const darkModeCookie = localStorage.getItem('darkMode');
    if (darkModeCookie ==='true') {
      this.$vuetify.theme.dark = true;
    }else{
      this.$vuetify.theme.dark = false;
    }
  },
  methods: {
    switchTheme() {
      this.$vuetify.theme.dark = !this.$vuetify.theme.dark;
      localStorage.setItem('darkMode', ''+this.$vuetify.theme.dark);
    },
  },
};
</script>