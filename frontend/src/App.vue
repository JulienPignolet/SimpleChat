<template>
  <div id="appVue">
      <my-header></my-header>

      <div class="content">
        <router-view></router-view>
      </div>
  </div>
</template>

<script>
import MyHeader from "./components/MyHeader.vue"

export default {
  components: { MyHeader }
};
</script>

<style lang="scss" src="./components/css/app.scss">
</style>