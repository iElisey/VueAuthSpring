<template>
  <v-app id="inspire">
    <v-app-bar app>
      <v-container class="py-0 fill-height">
        <v-app-bar-nav-icon class="mr-3" @click="drawer = true"></v-app-bar-nav-icon>
        <router-link class="text-decoration-none mr-2" to="/">
          <v-btn link :disabled="$route.path==='/'">
            Main
          </v-btn>
        </router-link>
        <v-spacer></v-spacer>

        <v-menu offset-y transition="scale-transition" nudge-width="195" max-height="75vh" class="overflow-auto ">
          <v-progress-linear
              :active="isLoading"
              :indeterminate="isLoading"
              absolute
          ></v-progress-linear>

          <template v-slot:activator="{ on, attrs }">
            <v-btn class="mr-2" @click.native="getStaff" v-on="on" v-bind="attrs" icon>
              <v-icon>
                mdi-security
              </v-icon>
            </v-btn>
          </template>
          <v-list>

            <v-subheader>
              Online staff: {{ staff.length }}
            </v-subheader>
            <div v-for="(item, index) in staff"
                 :key="index">

              <v-divider></v-divider>
              <v-list-item link @click.native="getUser(item.username)">
                <v-list-item-avatar class="rounded-sm">
                  <img :src="'//skin.vimeworld.com/head/'+item.username+'.png'" alt="">
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title :style="'color:'+ getColorRank(item.rank)">
                    {{ item.username }}
                  </v-list-item-title>
                  <v-list-item-subtitle>
                    Онлайн
                  </v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </div>
          </v-list>

        </v-menu>
        <div v-if="currentUser" class="mr-2">
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-btn style="text-transform: none" v-on="on" v-bind="attrs" link>
                <v-img width="30" height="30"
                       :src="'https://skin.vimeworld.com/head/'+currentUser.username+'.png'" alt="avatar User"></v-img>
                &nbsp;<span v-if="!isMobile"> {{ currentUser.username }}</span>
              </v-btn>
            </template>
            <v-list dense>
              <router-link class="text-decoration-none" to="/profile">
                <v-list-item link>
                  <v-icon>mdi-account</v-icon>&nbsp;Profile
                </v-list-item>
              </router-link>
              <div @click.stop="dialog=true" class="text-decoration-none">
                <v-list-item class="red--text" link>
                  <v-icon color="red">mdi-logout</v-icon>&nbsp;Logout
                </v-list-item>
                <v-dialog
                    v-model="dialog"
                    max-width="280"
                >
                  <v-card>

                    <v-card-title class="text-h6">
                      You really wan't logout?
                    </v-card-title>
                    <v-card-actions>

                      <v-spacer></v-spacer>

                      <v-btn
                          color="primary darken-1"
                          text
                          href="logout">
                          YES
                      </v-btn>
                      <v-btn text color="error darken-1" @click="dialog = false">
                        CANCEL
                      </v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </div>

            </v-list>
          </v-menu>


        </div>
        <div v-if="!currentUser">
          <router-link class="text-decoration-none" to="/login">

            <v-btn link :disabled="$route.path==='/login'">
              LogIn
            </v-btn>
          </router-link>
        </div>
        <div v-if="currentUser">

        </div>
      </v-container>
      <!--      <v-progress-linear absolute-->
      <!--                         bottom :indeterminate="isLoadingUser"-->
      <!--                         :active="isLoadingUser"></v-progress-linear>-->
    </v-app-bar>
    <v-navigation-drawer
        v-model="drawer"
        absolute
        temporary>
      <v-list>
        <v-list-item>
          <ColorThemeSwitch/>
          - Change theme
        </v-list-item>
        <v-divider></v-divider>

      </v-list>
    </v-navigation-drawer>
    <v-main>
      <keep-alive>
        <router-view ref="view"/>
      </keep-alive>
    </v-main>
  </v-app>
</template>

<script>
import {default as axios} from "axios";
import ColorThemeSwitch from "./components/ColorThemeSwitch";
import UserService from "./services/user.service";

export default {
  name: 'App',
  components: {
    ColorThemeSwitch,
  },
  data: () => ({
    staff: [],
    fab: false,
    drawer: false,
    isLoading: false,
    isLoadingUser: false,
    isMobile: false,
    dialog: false,

  }),
  computed: {
    currentUser() {
      return this.$store.state.auth.user;
    },
    snackbar: {
      get() {

        return this.$store.state.snackbar;
      },
      set(value, err) {
        this.$store.commit('updateSnackbar', value);
        this.$store.commit('updateError', err);
      },
    },
    err: {
      get() {
        return this.$store.state.err;
      },
      set(value, err) {
        this.$store.commit('updateSnackbar', value);
        this.$store.commit('updateError', err);
      },
    },

  },
  beforeDestroy() {
    if (typeof window !== 'undefined') {
      window.removeEventListener('resize', this.onResize, {passive: true})
    }
  },

  mounted() {
    this.onResize()
    window.addEventListener('resize', this.onResize, {passive: true})
  },
  methods: {
    showNotification(text) {
      this.$toast.error(text, {
        position: "top-right",
        timeout: 5000,
        closeOnClick: true,
        pauseOnFocusLoss: true,
        pauseOnHover: true,
        draggable: true,
        draggablePercent: 0.6,
        showCloseButtonOnHover: false,
        hideProgressBar: false,
        closeButton: "button",
        icon: true,
        rtl: false
      });
    },
    logOut() {
      this.$router.push('/login');
    },
    async getStaff() {
      this.isLoading = true;
      axios.get('https://api.vimeworld.com/online/staff', {
        headers: {
          'Access-Token': 'DWE9uc6VpMeuCm75mCooD3sDSVsWulC'
        }
      })
          .then((response) => {
            if (response.data != null) {
              this.staff = response.data;
            } else {
              this.staff = null;
            }
            this.isLoading = false;
          })
          .catch((error) => {
            if (error.response) {
              switch (error.response.status) {
                case 400:
                case 403:
                case 404:
                  this.err = 'Invalid username!';
                  this.showNotification(this.err);

                  break;
                case 500:
                  this.err = 'Something is wrong, please try again later or reload the page.';
                  this.showNotification(this.err);
                  break;
                case 503:
                  this.err = 'Something wrong with VimeWorld API, please try again later';
                  this.showNotification(this.err);
                  break;
                default:

                  if (error.response.data != null) {
                    this.err = error.response.data;
                    this.showNotification(this.err);
                  } else {
                    this.err = error.message;
                    this.showNotification(this.err);
                  }
              }
            } else if (error.request) {
              this.err = error.request;
              this.showNotification(this.err);

            } else {
              this.err = error.message;
              this.showNotification(this.err);

            }
            this.isLoading = false;
          });


    },
    isLoadingUserUpdate() {
      this.isLoadingUser = this.$refs.user.isLoadingUser
    },
    onResize() {
      this.isMobile = window.innerWidth < 760
    },
    getUser(username) {
      if (this.$route.path === '/') {
        this.$refs.view.getUser(username);
      } else {
        this.$router.push('/', () => setTimeout(() => this.$refs.view.getUser(username), 10))

      }
    },
    to(username) {
      this.$refs.view.getUser(username);
    },
    getColorRank(rank) {
      return '#' + UserService.getRank(rank).color;
    },
  }
};

</script>
<style>
.pointer {
  cursor: pointer;
}
</style>