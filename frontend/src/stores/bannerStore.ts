import { ref } from 'vue';

const showbanner = ref(false);
const isError = ref(false);
const text = ref("");

const showBannerMessage = (message: string, e: boolean) => {
  showbanner.value = true;
  isError.value = e;
  text.value = message;
  console.log("Banner message:", message);
  setTimeout(()=>{
    showbanner.value = false;
    text.value = "";
  }, 4000);
};

export const useBannerStore = () => ({
    showbanner,
    text,
    isError,
    showBannerMessage
});
