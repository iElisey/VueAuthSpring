<template>

  <v-container>
    <v-row>
      <v-col>
        <v-text-field clearable @keydown.enter="getUser(username)" v-model="username"
                      placeholder="Write username" flat label="Find user">

        </v-text-field>
      </v-col>
      <v-col cols="4" md="2">
        <v-btn :loading="isLoadingUser || isLoadingFriends || isLoadingAchievements" block color="primary"
               @click="getUser(username)">Search
        </v-btn>
      </v-col>
    </v-row>

    <add-favorite @getFavoriteUser="getUser($event)"></add-favorite>

    <v-card class="mt-3">
      <v-progress-linear v-show="false" :active="isLoadingUser" :indeterminate="isLoadingUser"></v-progress-linear>
      <v-tabs fixed-tabs show-arrows v-model="tab">
        <v-tabs-slider></v-tabs-slider>
        <v-tab :href="`#tab-${key + 1}`"
               v-for="(tabName, key) in tabItems"
               :key="key"
               class="primary--text">
          {{ tabName }}
        </v-tab>

      </v-tabs>
      <v-tabs-items v-model="tab">
        <v-tab-item value="tab-1">
          <v-card>
            <v-container pt-0 pb-0 fluid grid-list-lg>
              <v-card-text>

                <div v-if="usernamea && usernamea.user && usernamea.online">


                  <div class="title mb-1" v-if="usernamea.user">
                    Stats player <span
                      :style="{color: getColorRank(usernamea.user.rank)}">{{
                      usernamea.user.username
                    }}</span>
                  </div>

                  <div class="d-sm-flex ">
                    <v-badge :color="usernamea.online.value ? 'green' : 'red'" top overlap bordered dot>
                      <v-tooltip bottom
                                 :color="usernamea.online.value ? 'green' : 'red'">
                        <template v-slot:activator="{ on, attrs }">
                          <v-img transition="scale-transition" v-bind="attrs"
                                 v-on="on"
                                 :src="'//skin.vimeworld.com/body/'+usernamea.user.username+'.png'"></v-img>
                        </template>
                        <h4>{{ usernamea.user.username }}</h4>
                        <span>{{ usernamea.online.message }}</span>

                      </v-tooltip>
                    </v-badge>
                    <v-card class="ml-sm-6 card-triangle">
                      <v-list dense>
                        <v-list-item @click="tabChange(item)"
                                     v-for="(item, i) in playerData"
                                     :key="i">
                          <v-list-item-icon>
                            <v-icon v-text="item.icon"></v-icon>
                          </v-list-item-icon>
                          <v-list-item-content>
                            <v-list-item-title
                                v-if="item.color == null && item.text!=='Amount of friends' && item.text!=='Achievements complete'">
                              {{ item.text + ': ' }} <span class="primary--text">{{ item.value }}</span>
                            </v-list-item-title>
                            <v-list-item-title v-if="item.color != null">{{ item.text + ': ' }} <span
                                v-text="item.value" v-bind:style="{ color: item.color}"></span>
                            </v-list-item-title>

                            <v-list-item-title v-if="item.text==='Amount of friends'">
                              {{ item.text + ': ' }}
                              <span class="primary--text" v-if="!isLoadingFriends" v-text="item.value"></span>
                              <v-progress-circular v-else
                                                   width="2"
                                                   :size="20"
                                                   indeterminate color="primary"
                                                   v-show="isLoadingFriends"
                              ></v-progress-circular>
                            </v-list-item-title>
                            <v-list-item-title v-if="item.text==='Achievements complete'">
                              {{ item.text + ': ' }}
                              <span class="primary--text" v-if="!isLoadingAchievements" v-text="item.value"></span>
                              <v-progress-circular v-else
                                                   width="2"
                                                   :size="20"
                                                   indeterminate color="primary"
                                                   v-show="isLoadingAchievements"
                              ></v-progress-circular>
                            </v-list-item-title>
                          </v-list-item-content>
                        </v-list-item>


                      </v-list>

                    </v-card>
                  </div>
                </div>
                <div v-else class="title">
                  {{ err }}
                </div>
              </v-card-text>
              <v-card-actions v-if="usernamea">
                <v-btn color="primary" @click="like" icon>
                  <v-icon>{{ iconLike }}</v-icon>
                </v-btn>
                <span class="primary--text">&nbsp;{{ likes }}&nbsp;</span>
                <v-spacer></v-spacer>
                <v-btn @click.stop="dialogLikes = true" color="primary" small>List of liked</v-btn>
                <v-dialog
                    v-model="dialogLikes"
                    max-width="290"
                >
                  <v-card>

                    <v-card-title class="text-h6">
                      Players liked {{ usernamea.user.username }}
                    </v-card-title>
                    <v-divider></v-divider>
                    <v-card-text>
                      <v-list class="overflow-auto" dense>
                        <v-list-item link @click="getUser(user.username)" v-for="(user,key) in playerLikes"
                                     :key="key">
                          <v-list-item-avatar rounded>
                            <v-img :src="'//skin.vimeworld.com/helm/3d/'+user.username+'.png'"></v-img>
                          </v-list-item-avatar>
                          <v-list-item-title>{{ user.username }}</v-list-item-title>
                        </v-list-item>
                      </v-list>
                    </v-card-text>
                  </v-card>
                </v-dialog>
              </v-card-actions>
            </v-container>
          </v-card>
        </v-tab-item>
        <v-tab-item value="tab-2">
          <v-card flat>
            <v-container pt-0 pb-0 fluid grid-list-lg>
              <v-card-text>
                <div v-if="usernamea!=null">
                  <div class="title mb-1">
                    Guild player <span :style="{color: getColorRank(usernamea.user.rank)}"> {{
                      usernamea.user.username
                    }}</span>
                  </div>
                  <v-row v-if="usernamea.user.guild!=null">
                    <v-col>
                      <div class="d-flex justify-start flex-row">
                        <div v-if="usernamea.user.guild.avatar_url !== null">
                          <v-img style="height: 100px; width: 100px;"
                                 :src="usernamea.user.guild.avatar_url"></v-img>

                        </div>
                        <div class="ml-2">
                          <h4> ID: <span class="primary--text">{{ usernamea.user.guild.id }}</span></h4>
                          <h4> Name: <span class="primary--text">{{ usernamea.user.guild.name }}</span></h4>
                          <h4> Tag: <span class="primary--text">{{
                              usernamea.user.guild.tag != null ? usernamea.user.guild.tag : 'none'
                            }}</span>
                          </h4>
                        </div>

                      </div>
                    </v-col>
                    <v-col sm="6" cols="12">
                      <more-guild :id-guild="usernamea.user.guild.id"></more-guild>
                    </v-col>
                  </v-row>

                  <div v-else>
                    <v-subheader>This player is not in a guild!</v-subheader>
                  </div>

                </div>
                <div v-else class="title">
                  {{ err }}
                </div>
              </v-card-text>
            </v-container>
          </v-card>
        </v-tab-item>
        <v-tab-item value="tab-3">
          <v-card>

            <v-container pt-0 pb-0 fluid grid-list-lg>
              <v-card-text>
                <v-expansion-panels v-if="usernamea">
                  <v-expansion-panel>
                    <v-expansion-panel-header>
                      <template v-slot:actions>
                        <v-icon color="primary">
                          $expand
                        </v-icon>
                      </template>
                      <div class="title">
                        Friends player
                        <span :style="{color: getColorRank(usernamea.user.rank)}"> {{ usernamea.user.username }}</span>
                        <span> - </span>
                        <span class="primary--text" v-if="!isLoadingFriends">{{ friends.length }}</span>
                        <v-progress-circular v-else
                                             indeterminate color="primary"
                                             v-show="isLoadingFriends">

                        </v-progress-circular>
                      </div>

                    </v-expansion-panel-header>
                    <v-expansion-panel-content>
                      <v-row class="wrap">
                        <v-col cols="12">
                          <v-row>
                            <v-col
                                class="d-flex"
                                v-if="friends.length>0"
                                lg="6"
                            >
                              <v-text-field v-model="searchFriend" label="Search"></v-text-field>
                            </v-col>
                            <v-col
                                class="d-flex"
                                v-if="friends.length>0"
                                lg="6"
                            >
                              <v-select
                                  :items="items"
                                  v-model="keySelect"
                                  @change="onChange(keySelect)"
                                  label="Sorting"
                              ></v-select>
                            </v-col>
                          </v-row>
                        </v-col>

                        <v-col xs="12" sm="6" md="4" lg="3" xl="2" :key="key2"
                               v-for="(friend,key2) in filteredList">
                          <v-lazy
                              v-model="friend.isActive" :options="{threshold: 1}"
                              class="fill-height" transition="scroll-x-transition">
                            <v-card v-if="!isLoadingFriends">
                              <div class="d-flex justify-start flex-row">

                                <div class="pa-2">
                                  <v-badge :color="friend.online.value ? 'green' : 'red'" top overlap bordered
                                           dot>
                                    <v-tooltip bottom
                                               :color="friend.online.value ? 'green' : 'red'">
                                      <template v-slot:activator="{ on, attrs }">
                                        <v-img
                                            lazy-src=""
                                            v-bind="attrs"
                                            v-on="on"
                                            style="height: 100px; width: 100px;"
                                            @mouseover="friend.skinType = 'head'"
                                            @mouseleave="friend.skinType = 'helm'"
                                            :src="'//skin.vimeworld.com/helm/3d/'+friend.username+'.png'"
                                            transition="fade-transition"
                                        >
                                          <template v-slot:placeholder>
                                            <v-row
                                                align="center"
                                                class="fill-height ma-0"
                                                justify="center"
                                            >
                                              <v-progress-circular
                                                  color="grey"
                                                  indeterminate
                                              ></v-progress-circular>
                                            </v-row>
                                          </template>
                                        </v-img>
                                      </template>
                                      <h4>{{ friend.username }}</h4>
                                      <span>{{ friend.online.message }}</span>

                                    </v-tooltip>
                                  </v-badge>
                                </div>

                                <div class="pa-2">
                                  <h4 class="pointer"
                                      @click="getUser(friend.username)"><span
                                      v-bind:style="{ color: getColorRank(friend.rank)}">{{
                                      getNameRank(friend.rank)
                                    }}{{
                                      friend.username
                                    }}</span></h4>
                                  <h4>ID: <span class="primary--text"> {{ friend.id }}</span></h4>
                                  <h4>Level: <span class="primary--text"> {{ friend.level }}</span></h4>
                                </div>

                              </div>
                            </v-card>
                          </v-lazy>
                        </v-col>
                        <template v-if="isLoadingFriends">
                          <v-col cols="6">
                            <v-skeleton-loader v-if="isLoadingFriends"
                                               type="list-item, divider"></v-skeleton-loader>
                          </v-col>
                          <v-col cols="6">
                            <v-skeleton-loader v-if="isLoadingFriends"
                                               type="list-item-avatar, divider"></v-skeleton-loader>
                          </v-col>
                          <v-col xs="12" sm="12" md="6" lg="4" xl="2" v-for="key3 in 25" :key="key3">
                            <v-skeleton-loader v-if="isLoadingFriends" elevation="4"
                                               type="article"
                            ></v-skeleton-loader>
                          </v-col>
                        </template>


                      </v-row>

                      <v-subheader class="mt-3" v-if="friends.length===0 && !isLoadingFriends">This is player don't have
                        friends!
                      </v-subheader>

                    </v-expansion-panel-content>

                  </v-expansion-panel>
                </v-expansion-panels>
                <div v-else class="title">
                  {{ err }}
                </div>
              </v-card-text>
            </v-container>
          </v-card>
        </v-tab-item>
        <v-tab-item value="tab-4">
          <v-card>
            <v-container pt-0 pb-0 fluid grid-list-lg>
              <v-card-text>
                <v-expansion-panels v-if="usernamea">

                  <v-expansion-panel>

                    <v-expansion-panel-header>
                      <template v-slot:actions>
                        <v-icon color="primary">
                          $expand
                        </v-icon>
                      </template>

                      <div class="title">
                        Achievements player
                        <span :style="{color: getColorRank(usernamea.user.rank)}"> {{ usernamea.user.username }}</span>
                        <span> - </span>
                        <span class="primary--text"
                              v-if="!isLoadingAchievements">{{ completeAchievements + '/' + allAchievements }}</span>
                        <v-progress-circular v-else
                                             indeterminate color="primary"
                                             v-show="isLoadingAchievements"
                        ></v-progress-circular>
                      </div>

                    </v-expansion-panel-header>
                    <v-expansion-panel-content>
                      <v-row class="wrap">
                        <template v-if="!isLoadingAchievements">
                          <v-col cols="12" :key="key2"
                                 v-for="(type,key2) in achievements">

                            <v-card v-if="!isLoadingAchievements" class="pa-4">
                              <h1 class="text-center mb-8 text--secondary">{{ key2 }}</h1>
                              <v-row class="row wrap p-3">
                                <v-col xs="12" sm="12" md="6" lg="4" xl="2" v-for="(achievement, key) in type"
                                       :key="key">
                                  <v-lazy
                                      v-model="achievement.isActive" :options="{threshold: 1}"
                                      class="fill-height" transition="scroll-x-transition">
                                    <v-alert
                                        :type="achievement.complete ? 'success' : 'error'">
                                      <div class="d-flex justify-start flex-row">
                                        <v-hover>
                                          <template>
                                            <v-scroll-x-transition>
                                              <div>
                                                <h3>{{ achievement.title }}</h3>
                                              </div>
                                            </v-scroll-x-transition>
                                          </template>
                                        </v-hover>
                                      </div>
                                    </v-alert>
                                  </v-lazy>
                                </v-col>
                              </v-row>
                            </v-card>
                          </v-col>
                        </template>
                        <template v-if="isLoadingAchievements">
                          <v-col cols="12" v-for="key in 8" :key="key">
                            <v-card class="pa-4" v-if="isLoadingAchievements">
                              <v-row>
                                <v-col cols="3" class="mx-auto">
                                  <v-skeleton-loader v-if="isLoadingAchievements" elevation="4"
                                                     type="list-item"
                                  ></v-skeleton-loader>
                                </v-col>
                              </v-row>
                              <v-row>
                                <v-col xs="12" sm="12" md="6" lg="4" xl="2" v-for="key3 in 25" :key="key3">
                                  <v-skeleton-loader v-if="isLoadingAchievements" elevation="4"
                                                     type="list-item-avatar"
                                  ></v-skeleton-loader>
                                </v-col>
                              </v-row>
                            </v-card>
                          </v-col>
                        </template>


                      </v-row>

                    </v-expansion-panel-content>

                  </v-expansion-panel>
                </v-expansion-panels>
                <div v-else class="title">
                  {{ err }}
                </div>


              </v-card-text>
            </v-container>
          </v-card>
        </v-tab-item>
        <v-tab-item value="tab-5">
          <v-card>
            <v-container pt-0 pb-0 fluid grid-list-lg>
              <v-card-text>
                <v-expansion-panels v-if="usernamea">

                  <v-expansion-panel>
                    <v-expansion-panel-header>
                      <template v-slot:actions>
                        <v-icon color="primary">
                          $expand
                        </v-icon>
                      </template>
                      <div class="title">
                        Stats player
                        <span :style="{color: getColorRank(usernamea.user.rank)}"> {{ usernamea.user.username }}</span>
                      </div>
                    </v-expansion-panel-header>
                    <v-expansion-panel-content>
                      <v-row class="wrap">
                        <template>
                          <v-col cols="12" :key="key2"
                                 v-for="(stat,key2) in stats">
                            <v-card class="pa-4">
                              <h1 class="text-center mb-8 text--secondary">{{ stat.name }}</h1>
                              <v-row class="row wrap p-3">
                                <!--                                <v-col xs="12" sm="12" md="6" lg="4" xl="2" v-for="(achievement, key) in name"-->
                                <!--                                       :key="key">-->
                                <!--                                    <v-alert-->
                                <!--                                        :type="achievement.complete ? 'success' : 'error'">-->
                                <!--                                      <div class="d-flex justify-start flex-row">-->
                                <!--                                        <v-hover>-->
                                <!--                                          <template>-->
                                <!--                                            <v-scroll-x-transition>-->
                                <!--                                              <div>-->
                                <!--                                                <h3>{{ achievement.title }}</h3>-->
                                <!--                                              </div>-->
                                <!--                                            </v-scroll-x-transition>-->
                                <!--                                          </template>-->
                                <!--                                        </v-hover>-->
                                <!--                                      </div>-->
                                <!--                                    </v-alert>-->
                                <!--                                </v-col>-->
                              </v-row>
                            </v-card>
                          </v-col>
                        </template>
                      </v-row>
                    </v-expansion-panel-content>
                  </v-expansion-panel>
                </v-expansion-panels>
                <div v-else class="title">
                  {{ err }}
                </div>


              </v-card-text>
            </v-container>
          </v-card>
        </v-tab-item>
      </v-tabs-items>
    </v-card>

  </v-container>

</template>

<script>
import AddFavorite from "../components/AddFavorite";
import UserService from "../services/user.service";
import moreGuild from "../components/MoreGuild";
import VimeService from "../services/vime.service";
import TimerService from "../services/timer.service";
import Timer from "../services/timer.service";

const axios = require('axios').default;

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'HelloWorld',
  components: {moreGuild, AddFavorite},

  data() {
    return {
      keySelect: null,
      tab: null,
      iconLike: 'mdi-cards-heart-outline',
      tabItems: ['PLAYER', 'GUILD', 'FRIENDS', 'ACHIEVES', 'STATS'],
      likedThisUser: false,
      usernamea: null,
      isLoadingUser: false,
      isLoadingOnline: false,
      isLoadingFriends: false,
      isLoadingAchievements: false,
      completeAchievements: 0,
      allAchievements: 0,
      dialogLikes: false,
      url: 'https://api.vimeworld.com',
      username: '',
      err: 'Enter username',
      friends: [],
      items: ['id', 'online', 'rank', 'level'],
      searchFriend: '',
      selectedItem: 1,
      playerData: [],
      achievements: [],
      id: -1,
      likes: 0,
      playerLikes: null,
      timer: null,
      doingTimer: false,
      stats:[],

    }
  },

  computed: {
    filteredList() {
      return this.friends.filter(friend => {
        return friend.username.toLowerCase().includes(this.searchFriend.toLowerCase())
      })
    },
    currentUser() {
      return this.$store.state.auth.user;
    },
    requestCount() {
      return this.$store.state.requestCount;
    },
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



    day(sec) {
      var min = sec / 60;
      var hour = min / 60;
      return Math.floor(hour / 24) + ' days ' + Math.floor(hour % 24) + ' hours '
          + Math.floor(min % 60) + ' minutes ' +
          Math.floor(sec % 60) + ' seconds';
    },
    like() {
      if (this.currentUser != null) {
        if (this.id === this.currentUser.id) {
          this.showNotification('You can like yourself!');
        } else {
          if (this.iconLike === 'mdi-cards-heart-outline') {
            UserService.like(this.id).then((response) => {
              this.likes = response.data.likes.length;
              this.playerLikes = response.data.likes;
              this.iconLike = 'mdi-cards-heart';
            });
          } else if (this.iconLike === 'mdi-cards-heart') {
            UserService.unlike(this.id).then((response) => {
              this.likes = response.data.likes.length;
              this.playerLikes = response.data.likes;
              this.iconLike = 'mdi-cards-heart-outline';
            });
          }
        }
        this.$store.commit('updateRequestCount', 1);
      } else {
        this.$router.push('/login');
      }
    },
    tabChange(item) {
      if (item.text === 'Amount of friends') {
        this.tab = 'tab-3';
      } else if (item.text === 'Achievements complete') {
        this.tab = 'tab-4'
      }
    },
    contains(arr, elem) {
      for (let i = 0; i < arr.length; i++) {
        const areEqual = arr[i].toUpperCase() === elem.toUpperCase();
        if (areEqual) {
          return true;
        }
      }
      return false;
    },
    onChange(a) {
      if (a === 'id') {
        this.keySelect = a;
        this.friends.sort((a, b) => b.id - a.id);
      } else if (a === 'online') {
        this.keySelect = a;

        this.friends.sort((a, b) => b.online.value - a.online.value);
      } else if (a === 'rank') {
        this.keySelect = a;


        this.friends.sort((a, b) => this.getValueRank(b.rank) - this.getValueRank(a.rank));
      } else if (a === 'level') {
        this.keySelect = a;

        this.friends.sort((a, b) => b.level - a.level);

      }
    },
    isValid(username) {
      return /^[a-zA-Z0-9_]+$/.test(username)
    },
    async getUser(value) {
      if (value != null && this.isValid(value)) {
        this.isLoadingUser = true;
        await axios.get(this.url + '/user/name/' + value, {
          headers: {
            'Access-Token': 'DWE9uc6VpMeuCm75mCooD3sDSVsWulC'
          }
        })
            .then(async (response) => {
              if (response.data.length === 0) {
                this.err = 'Player not found!';
                this.showNotification(this.err);

              }
              else if (response.data.error != null) {
                switch (response.data.error.error_code) {
                  case -3:
                  case -2:
                    this.err = 'Something wrong with VimeWorld API, please try again later';
                    break;
                  case -1:
                    this.err = 'Invalid username!';
                    break;
                  case 2:
                    this.err = 'The number of requests has exceeded the limit (300), please try again in a few seconds.';
                    break;
                  case 1:
                  case 3:
                  case 4:
                    this.err = 'Something is wrong, please try again later or reload the page.';
                    break;

                }
                this.showNotification(this.err);


              }
              else {
                response.data.forEach(el => this.id = el.id);
                UserService.getLikes(this.id).then((response) => {
                  this.iconLike = 'mdi-cards-heart-outline';
                  if (this.currentUser != null) {
                    response.data.forEach(el => {
                      if (el.id === this.currentUser.id) {
                        this.iconLike = 'mdi-cards-heart';
                      }
                    })
                  }
                  this.playerLikes = response.data;
                  this.likes = response.data.length
                });
                this.isLoadingOnline = true;
                await this.getSessionPlayer(response.data);
                this.isLoadingUser = false;
                this.isLoadingOnline = false;

                this.isLoadingFriends = true;
                await this.getFriends();
                this.isLoadingAchievements = true;
                await this.getAchievements();
                await this.getLocaleStatsNames();

              }


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
                    } else {
                      this.err = error.message;
                    }
                    this.showNotification(this.err);

                }
              } else if (error.request) this.err = error.request
              else this.err = error.message;

              this.showNotification(this.err);


            });

      } else {
        this.err = 'Nickname incorrect';
        this.showNotification(this.err);

      }

      this.isLoadingUser = false;
      this.isLoadingOnline = false;
      this.isLoadingFriends = false;
      this.isLoadingAchievements = false;


    },
    async getSessionPlayer(data) {
      let data2 = null;

      data.forEach(el => data2 = el);

      await axios.get(this.url + '/user/' + data2.id + '/session', {
        headers: {
          'Access-Token': 'DWE9uc6VpMeuCm75mCooD3sDSVsWulC'
        }
      })
          .then((response) => {
            this.usernamea = response.data;
            this.usernamea.user.playedSeconds = this.day(this.usernamea.user.playedSeconds);

            this.usernamea.user.lastSeen = VimeService.toISODate(this.usernamea.user.lastSeen);
            this.playerData = [];
            this.playerData.push({value: this.usernamea.user.id, icon: 'mdi-card-account-details', text: 'ID'})
            this.playerData.push({value: this.usernamea.user.level, icon: 'mdi-finance', text: 'Level'})
            this.playerData.push({
              value: this.usernamea.user.playedSeconds,
              icon: 'mdi-clock-time-nine',
              text: 'Played time'
            })
            this.playerData.push({value: this.usernamea.user.lastSeen, icon: 'mdi-exit-run', text: 'Last seen'})
            this.playerData.push({
              value: this.usernamea.user.rank,
              icon: 'mdi-account-arrow-up',
              text: 'Rank',
              color: this.getColorRank(this.usernamea.user.rank)
            })
            this.playerData.push({text: 'Amount of friends', icon: 'mdi-account-group', value: 0})
            this.playerData.push({text: 'Achievements complete', icon: 'mdi-trophy', value: 0})

          })
          .catch((error) => {
            if (!error.response.data.error.error_msg) {
              this.err = error.response.data.error.error_msg;


            } else {
              this.err = 'Error';


            }
            this.showNotification(this.err);

          });

    },
    async getFriends() {
      this.friends = [];

      await axios.get(this.url + '/user/' + this.usernamea.user.id + '/friends', {
        headers: {
          'Access-Token': 'DWE9uc6VpMeuCm75mCooD3sDSVsWulC'
        }
      })
          .then((response) => {
            let ids = [];
            response.data.friends.forEach(el => ids.push(el.id));
            if (ids.length !== 0) {
              this.getSessionFriend(ids);
            } else {
              this.friends = [];
              this.isLoadingFriends = false;
            }
          })
          .catch((error) => {
            this.err = error.response.data.error.error_msg;
            this.showNotification(this.err);


          })


    },
    async getSessionFriend(ids) {

      await axios.post(this.url + '/user/session', ids, {
        headers: {
          'Access-Token': 'DWE9uc6VpMeuCm75mCooD3sDSVsWulC'
        }
      })
          .then((response) => {
            this.friends = response.data;
            this.friends.forEach(el => {
              el.skinType = 'helm';
              el.isActive = false
            });
            this.friends.sort((a, b) => b.online.value - a.online.value);
            this.keySelect = 'online';
            this.searchFriend = '';
            this.playerData.forEach(el => {
              if (el.text === 'Amount of friends') {
                el.value = this.friends.length;
              }
            })

          })
          .catch((error) => {
            if (!error.response.data.error.error_msg) {
              this.err = error.response.data.error.error_msg;

            } else {
              this.err = 'Error';

            }
            this.showNotification(this.err);

          });

      this.isLoadingFriends = false;

    },
    async getAchievementsUser() {
      await axios.get(this.url + '/user/' + this.usernamea.user.id + '/achievements', {
        headers: {
          'Access-Token': 'DWE9uc6VpMeuCm75mCooD3sDSVsWulC'
        }
      })
          .then((response) => {
            for (let key in this.achievements) {

              let value = this.achievements[key];
              if (value instanceof Object) {
                for (let key2 in value) {

                  response.data.achievements.forEach(playerAchievement => {
                    value[key2].isActive = false;
                    if (playerAchievement.id === value[key2].id) {
                      value[key2].complete = true;
                      this.completeAchievements++;
                    }
                  })
                }
              }
            }

            this.playerData.forEach(el => {
              if (el.text === 'Achievements complete') {
                el.value = this.completeAchievements + '/' + this.allAchievements;
              }
            })

            this.isLoadingAchievements = false;
          })
          .catch((error) => {
            this.err = error.response.data.error.error_msg;
            this.showNotification(this.err);


          })
      this.isLoadingAchievements = false;

    },
    async getAchievements() {
      this.completeAchievements = 0;
      this.allAchievements = 0;
      this.isLoadingAchievements = true;
      await axios.get(this.url + '/misc/achievements', {
        headers: {
          'Access-Token': 'DWE9uc6VpMeuCm75mCooD3sDSVsWulC'
        }
      })
          .then((response) => {
            this.achievements = response.data;
            for (const key in this.achievements) {
              let value = this.achievements[key];
              if (value instanceof Object) {
                // eslint-disable-next-line no-unused-vars
                for (let key2 in value) {
                  this.allAchievements++;
                }
              }
            }
            this.getAchievementsUser();

          })
          .catch((error) => {
            this.err = error.response.data.error.error_msg;
            this.showNotification(this.err);

          })
    },
    async getLocaleStatsNames() {
      VimeService.getLocaleNames().then((response) => {
        let gameNames = response.data.games;
        let game_stats = response.data.game_stats;
        for (var key in gameNames) {
          this.stats.push({'name': gameNames[key].name});
        }
      })
      console.log(this.stats)
    },
    sortById() {
      this.friends.sort((a, b) => b.id - a.id);
    },
    getColorRank(rank) {
      return '#' + UserService.getRank(rank).color;
    },
    getNameRank(rank) {
      return UserService.getRank(rank).name;
    },
    getValueRank(rank) {
      return UserService.getRank(rank).value;
    },

    copy(type) {
      this.$refs.clone.focus();
      document.execCommand('copy');
    }

  }
}
</script>
<style scoped>
.v-card--reveal {
  bottom: 0;
  opacity: 1 !important;
  position: absolute;
  width: 100%;
}

img {
  max-width: 100%;
  height: auto;
}

li {
  list-style-type: none;
}

.value {
  opacity: 0.6;
}

.pointer {
  cursor: pointer;
}

.bold {
  font-weight: bold;
}
</style>
