<script setup lang="ts">
import { onLaunch, onShow, onHide } from "@dcloudio/uni-app";
import { useAppStore } from "@/store";
const appStore = useAppStore();

onLaunch(() => {
  console.log("App Launch");
  appStore.getUnitList();
  appStore.getCategoryList();
});
onShow(() => {
  console.log("App Show");
});
onHide(() => {
  console.log("App Hide");
});
</script>
<style lang="scss">
page {
  // height: 100%;
  font-family: "San Francisco", Rotobo, arial, "PingFang SC", "Noto SansCJK", "Microsoft Yahei", sans-serif;
}
</style>