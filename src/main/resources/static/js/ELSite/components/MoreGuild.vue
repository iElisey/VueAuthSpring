<template>
  <div>
    <v-btn v-show="!members" @click="getGuild" color="primary">MORE</v-btn>
    <v-card class="mb-2" v-show="fullGuild" v-if="fullGuild">
      <v-card-title>Additional information</v-card-title>
      <v-card-text><h4>Created: <span class="primary--text">{{ fullGuild.created }}</span></h4></v-card-text>
    </v-card>
    <v-card v-show="members">
      <v-card-title>
        Members guild
      </v-card-title>
      <v-card-text>
        <v-row class="wrap">
          <v-col cols="12">
            <v-card-title>Leader</v-card-title>
            <v-divider></v-divider>

          </v-col>
          <v-col v-if="leader" xs="12" sm="12" md="6">
            <v-lazy :options="{threshold: 1}"
                    class="fill-height" transition="scroll-x-transition">
              <v-card>
                <div class="d-flex justify-start flex-row">

                  <div class="pa-2">
                    <v-badge :color="'red'" top overlap bordered
                             dot>
                      <v-tooltip bottom
                                 :color="'red'">
                        <template v-slot:activator="{ on, attrs }">
                          <v-img
                              aspect-ratio="1"
                              class="grey lighten-2"
                              lazy-src=""
                              v-bind="attrs"
                              v-on="on"
                              style="height: 100px; width: 100px;"
                              :src="'//skin.vimeworld.com/head/'+leader.user.username+'.png'"
                              transition="fade-transition"
                          >
                            <template v-slot:placeholder>
                              <v-row
                                  align="center"
                                  class="fill-height ma-0"
                                  justify="center"
                              >
                                <v-progress-circular
                                    color="grey lighten-5"
                                    indeterminate
                                ></v-progress-circular>
                              </v-row>
                            </template>
                          </v-img>
                        </template>
                        <h4>{{ leader.user.username }}</h4>
                        <span>Soon</span>

                      </v-tooltip>
                    </v-badge>
                  </div>

                  <div class="pa-2">
                    <h4 class="pointer"
                    ><span
                        v-bind:style="{ color: getColorRank(leader.user.rank)}">{{
                        getNameRank(leader.user.rank)
                      }}{{
                        leader.user.username
                      }}</span></h4>
                    <h4>ID: <span class="primary--text"> {{ leader.user.id }}</span></h4>
                    <h4>Level: <span class="primary--text"> {{ leader.user.level }}</span></h4>
                  </div>

                </div>
              </v-card>
            </v-lazy>
          </v-col>

          <v-col cols="12">
            <v-card-title>Officers</v-card-title>
            <v-divider></v-divider>
          </v-col>
          <v-col v-if="officers" xs="12" sm="12" md="6" :key="'A'+key"
                 v-for="(officer,key) in officers">
            <v-lazy
                :options="{threshold: 1}"
                class="fill-height" transition="scroll-x-transition">
              <v-card>
                <div class="d-flex justify-start flex-row">
                  <div class="pa-2">
                    <v-badge :color="'red'" top overlap bordered
                             dot>
                      <v-tooltip bottom
                                 :color="'red'">
                        <template v-slot:activator="{ on, attrs }">
                          <v-img
                              aspect-ratio="1"
                              class="grey lighten-2"
                              lazy-src=""
                              v-bind="attrs"
                              v-on="on"
                              style="height: 100px; width: 100px;"
                              :src="'//skin.vimeworld.com/head/'+officer.user.username+'.png'"
                              transition="fade-transition"
                          >
                            <template v-slot:placeholder>
                              <v-row
                                  align="center"
                                  class="fill-height ma-0"
                                  justify="center"
                              >
                                <v-progress-circular
                                    color="grey lighten-5"
                                    indeterminate
                                ></v-progress-circular>
                              </v-row>
                            </template>
                          </v-img>
                        </template>
                        <h4>{{ officer.user.username }}</h4>
                        <span>Soon</span>
                      </v-tooltip>
                    </v-badge>
                  </div>
                  <div class="pa-2">
                    <h4 class="pointer"><span
                        v-bind:style="{ color: getColorRank(officer.user.rank)}">{{
                        getNameRank(officer.user.rank)
                      }}{{
                        officer.user.username
                      }}</span></h4>
                    <h4>ID: <span class="primary--text"> {{ officer.user.id }}</span></h4>
                    <h4>Level: <span class="primary--text"> {{ officer.user.level }}</span></h4>
                  </div>

                </div>
              </v-card>
            </v-lazy>
          </v-col>

          <v-col cols="12">

            <v-card-title>Members</v-card-title>
            <v-divider></v-divider>

          </v-col>
          <v-col v-if="members" xs="12" sm="12" md="6" :key="'B'+key"
                 v-for="(member,key) in members">
            <v-lazy
                :options="{threshold: 1}"
                class="fill-height" transition="scroll-x-transition">
              <v-card>
                <div class="d-flex justify-start flex-row">

                  <div class="pa-2">
                    <v-badge :color="'red'" top overlap bordered
                             dot>
                      <v-tooltip bottom
                                 :color="'red'">
                        <template v-slot:activator="{ on, attrs }">
                          <v-img
                              aspect-ratio="1"
                              class="grey lighten-2"
                              lazy-src=""
                              v-bind="attrs"
                              v-on="on"
                              style="height: 100px; width: 100px;"
                              :src="'//skin.vimeworld.com/head/'+member.user.username+'.png'"
                              transition="fade-transition"
                          >
                            <template v-slot:placeholder>
                              <v-row
                                  align="center"
                                  class="fill-height ma-0"
                                  justify="center"
                              >
                                <v-progress-circular
                                    color="grey lighten-5"
                                    indeterminate
                                ></v-progress-circular>
                              </v-row>
                            </template>
                          </v-img>
                        </template>
                        <h4>{{ member.user.username }}</h4>
                        <span>Soon</span>

                      </v-tooltip>
                    </v-badge>
                  </div>
                  <div class="pa-2">
                    <h4 class="pointer"><span
                        v-bind:style="{ color: getColorRank(member.user.rank)}">{{
                        getNameRank(member.user.rank)
                      }}{{
                        member.user.username
                      }}</span></h4>
                    <h4>ID: <span class="primary--text"> {{ member.user.id }}</span></h4>
                    <h4>Level: <span class="primary--text"> {{ member.user.level }}</span></h4>
                  </div>

                </div>
              </v-card>
            </v-lazy>
          </v-col>


        </v-row>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
import VimeService from "../services/vime.service";
import UserService from "../services/user.service";

export default {
  name: "MoreGuild",
  data() {
    return {
      members: null,
      officers: null,
      leader: null,
      fullGuild: null,
    }
  },
  props: ['idGuild'],
  watch: {
    idGuild() {
      this.members= null
      this.officers= null
      this.leader= null
      this.fullGuild= null
    },
  },
  methods: {
    getGuild() {
      this.leader = null
      this.members = [];
      this.officers = [];
      this.fullGuild = null
      console.log(this.idGuild)
      VimeService.getGuild(this.idGuild).then((response) => {
        this.fullGuild = response.data;
        this.fullGuild.created = VimeService.toISODate(this.fullGuild.created);
        response.data.members.forEach(user => {
          if (user.status === 'OFFICER') {
            this.officers.push(user);
          } else if (user.status === 'MEMBER') {
            this.members.push(user);
          } else if (user.status === 'LEADER') {
            this.leader = user;
          }
        })
        console.log(this.leader);

      });
    },
    getColorRank(rank) {
      return '#' + UserService.getRank(rank).color;
    },
    getNameRank(rank) {
      return UserService.getRank(rank).prefix;
    },
  }
}
</script>

<style scoped>

</style>