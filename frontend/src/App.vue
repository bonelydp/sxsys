<template>
  <div class="container">
    <router-view></router-view>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();
import request from "./utils/request"
import { nextTick, onMounted } from 'vue'

const debounce = (fn, delay) => {
  let timer = null;
  return function () {
    let context = this;
    let args = arguments;
    clearTimeout(timer);
    timer = setTimeout(function () {
      fn.apply(context, args);
    }, delay);
  }
}

const _ResizeObserver = window.ResizeObserver;
window.ResizeObserver = class ResizeObserver extends _ResizeObserver{
  constructor(callback) {
    callback = debounce(callback, 16);
    super(callback);
  }
}

onMounted(async () => {
  await nextTick()
})

</script>

<style scoped>

</style>