<template>
  <div>
    <v-chip-group>

      <v-chip v-for="(favorite, key) in favorites" @click="$emit('getFavoriteUser', favorite.username)"
              :key="key" class="white--text" :color="getColorRank(favorite.rank)">
        <span v-text="favorite.username"></span>
      </v-chip>
      <v-chip @click.stop="dialogFavorite = true" color="primary">
        Add favorite
      </v-chip>
    </v-chip-group>

    <v-dialog
        v-model="dialogFavorite"
        max-width="280"
    >
      <v-card>

        <v-card-title class="text-h5">
          Favorites
        </v-card-title>
        <v-card-text>
          <v-chip-group column>
            <v-chip  v-for="(favorite, key) in favorites" :key="key" color="red"
                    @click:close="deleteFavorite(favorite)" outlined close close-icon="mdi-delete">
              <span v-text="favorite.username"></span>
            </v-chip>
          </v-chip-group>
        </v-card-text>
        <v-card-title>Add favorite</v-card-title>
        <v-card-text>
          <v-text-field @keydown.enter="addFavorite" clearable ref="favoriteUsername" :counter="16"
                        v-model="favoriteUsername"
                        placeholder="Write username" flat label="Write username">

          </v-text-field>

        </v-card-text>
        <v-card-actions>

          <v-spacer></v-spacer>

          <v-btn
              color="primary darken-1"
              text
              @click="addFavorite()"
              :disabled="favorites.length===10"
          >
            Add
          </v-btn>
          <v-btn text color="error darken-1" @click="dialogFavorite = false">
            CANCEL
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

  </div>
</template>

<script>
import {default as axios} from "axios"
import UserService from "../services/user.service";

export default {
  name: "AddFavorite",
  data() {
    return {
      favoriteUsername: '',
      favorites: [],
      url: 'https://api.vimeworld.com',
      dialogFavorite: false,
      err: '',

    }
  },
  created() {
    if (localStorage.getItem('names') != null) {
      this.favorites = JSON.parse(localStorage.getItem('names'))
      this.favorites.forEach(el => {
        if (!el instanceof Object) {
          localStorage.setItem('names', null);
          this.$toast.info('We have removed your favorite players due to a storage change.', {
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
          })        }
      });
    }
  },
  methods: {
    getColorRank(rank) {
      return '#' + UserService.getRank(rank).color;
    },
    isValid(username) {

      return /^[a-zA-Z0-9_]+$/.test(username)
    },
    showNotificationError(text) {
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
      })
    },
    showNotificationSuccess(text) {
      this.$toast.success(text, {
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
      })
    },
    contains(arr, elem) {
      for (let i = 0; i < arr.length; i++) {
        const areEqual = arr[i].username.toUpperCase() === elem.toUpperCase()
        if (areEqual) {
          return true
        }
      }
      return false
    },
    deleteFavorite(favorite) {
      let index = this.favorites.indexOf(favorite)
      if (index !== -1) {
        this.showNotificationSuccess('Player ' + favorite.username + ' deleted successfully!')
        this.favorites.splice(index, 1)
      }
      localStorage.setItem('names', JSON.stringify(this.favorites))
    },
    addFavorite() {
      let username = this.favoriteUsername.replaceAll(' ', '')
      if (username !== '' && this.isValid(username) && this.favorites.length < 10 && !this.contains(this.favorites, username)) {

        axios.get(this.url + '/user/name/' + username, {
          headers: {
            'Access-Token': 'DWE9uc6VpMeuCm75mCooD3sDSVsWulC'
          }
        })
            .then(async (response) => {
              if (response.data.length === 0) {
                this.err = 'Player not found!'
                this.showNotificationError(this.err)
              } else if (response.data.error != null) {
                console.log(response.data)
                switch (response.data.error.error_code) {
                  case -3:
                  case -2:
                    this.err = 'Something wrong with VimeWorld API, please try again later'
                    break
                  case -1:
                    this.err = 'Invalid username!'
                    break
                  case 2:
                    this.err = 'The number of requests has exceeded the limit (300), please try again in a few seconds.'
                    break
                  case 1:
                  case 3:
                  case 4:
                    this.err = 'Something is wrong, please try again later or reload the page.'
                    break

                }
                this.showNotificationError(this.err)

              } else {
                let usernameCorrect = ''
                let rank = ''
                response.data.forEach(el => {
                  usernameCorrect = el.username
                  rank = el.rank
                })
                this.favorites.push({
                  'username': usernameCorrect,
                  'rank': rank
                })
                this.showNotificationSuccess('Player ' + usernameCorrect + ' added to favorites successfully!')
                localStorage.setItem('names', JSON.stringify(this.favorites))
                this.favoriteUsername = ''
              }
            })
      } else if (this.contains(this.favorites, username)) {
        this.err = 'This is user already added to favorites!'
        this.showNotificationError(this.err)
      } else if (this.favorites.length === 10) {
        this.err = 'Size of favorites = 10!'
        this.showNotificationError(this.err)
      } else {
        this.err = 'Invalid username!'
        this.showNotificationError(this.err)
      }
    },
  }
}
</script>

<style scoped>

</style>