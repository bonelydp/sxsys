<template>
  <footer class="footer">
    <div class="footer-content">

      <div>
        <!-- Logo and title -->
        <div class="logo">
          <img src="@/assets/Logo.png" alt="Logo">
          
          <div class="logo-titles">
            <h3>隐孢子虫病原监测与分析平台</h3>
            <span>CryptoMonitor</span>
          </div>
    
        </div>
        
        <!-- Contact information -->
        <div class="contact-info">
          
          <div class="contact-item">
            <img src="@/assets/phone.png" alt="phone" class="contact-icon">
            <p>1234-5678-910</p>
          </div>
          <div class="contact-item">
            <img src="@/assets/message.png" alt="email" class="contact-icon">
            <p>Cryptosp_scan.com</p>
          </div>
          <div class="contact-item">
            <img src="@/assets/place.png" alt="address" class="contact-icon">
            <p>广东省广州市天河区华南农业大学</p>
          </div>
        </div>
      </div>


      <!-- Links -->
      <div class="links">
        <div class="link-group">
          <h4>{{ t('message.dataResource') }}</h4>
          <ul>
            
            <li>
              <RouterLink :to="'/resourceshare'">
                SSU / RNA Datebase
              </RouterLink>
            </li>
            <li>
              <RouterLink :to="'/resourceshare'">
                gp40 Datebase
              </RouterLink>
            </li>
          </ul>
        </div>
        <div class="link-group">
          <h4>{{ t('message.hotDatabase') }}</h4>
          <ul>
            <li><a href="#">NCBI</a></li>
            <li><a href="#">CryptoDB</a></li>
            <li><a href="#">CHGdb</a></li>
          </ul>
        </div>
        <div class="link-group">
          <h4>{{ t('message.teamInformation') }}</h4>
          <ul>
            <li>
              <RouterLink :to="'/teamIntroduction'">
                {{t('message.teamIntroduction')}}
              </RouterLink>
            </li>
            <li>
              <RouterLink :to="'/notifications'">
                {{t('message.news')}}
              </RouterLink>
            </li>
          </ul>
        </div>
        <div class="link-group">
          <h4>{{ t('message.cooperativeUnits') }}</h4>
          <ul>
            <li><a href="#">华南农业大学</a></li>
            
          </ul>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { RouterLink } from 'vue-router';
const { t } = useI18n();
</script>

<style scoped>
.footer {
  background-color: #333;
  color: white;
  padding: 20px 0;
  margin-top:100px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  font-size: 0.9em;
}

.logo {
  display: flex;
  align-items: center;
}

.logo img {
  height: 30px;
  margin-right: 10px;
}

.logo-titles {
  display: flex;
  flex-direction: column;
}

.logo-titles h3 {
  margin: 0;
  font-size: 1.2em;
}

.logo-titles span {
  font-size: 0.9em;
  margin-top: 4px;
}

.contact-info {
  text-align: left;
}

.links {
  display: flex;
  gap: 20px;
}

.link-group {
  width: 200px;
}

.link-group h4 {
  font-weight: bold;
  margin-bottom: 10px;
  margin-left: 20px;
}

.link-group ul {
  list-style-type: none;
  padding: 0;
}

.link-group ul li {
  margin-bottom: 5px;
}

.link-group ul li a {
  color: white;
  text-decoration: none;
}
.link-group li::before{
  content: '>';
    margin-right: 10px;
    color: #ffffff;
}

a {
  text-decoration: none;
  color: inherit;
}

a:hover {
  color: #ffffff;
}


/* ------------------- */
.contact-item {
  display: flex;
  align-items: center;
  margin: 8px 0;
  gap: 8px;
}

.contact-icon {
  width: 16px;
  height: 16px;
  object-fit: contain;
  filter: invert(0); /* 将图标颜色转换为白色 */
}

.contact-item p {
  margin: 0;
}

.contact-info {
  text-align: left;
  margin-top: 15px;
}

.contact-info span {
  display: block;
  margin-bottom: 10px;
  font-weight: bold;
}
</style>