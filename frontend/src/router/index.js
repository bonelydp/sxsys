// router.js
import { createRouter, createWebHistory } from 'vue-router';
import AppLayout from '@/components/AppLayout'
import AdminAppLayout from '@/adminPages/AdminAppLayout.vue'
import LoginVc from '@/components/LoginVc'

import HomeVc from '../pages/HomeVc';
import PeopleCenterVc from '../pages/PeopleCenterVc';

import TeamIntroductionVc from '../pages/TeamIntroductionVc';
import TextVc from '@/pages/TextVc';


import DataVisualizationVc from '../pages/DataVisualizationVc';
import DataSubmitVc from '../pages/DataSubmitVc';
import ResultVc from '../textPages/ResultVc.vue';


import MafftVc from '../pages/MafftVc';


import ContactVc from '../pages/ContactVc';
import AwardVc from '../textPages/AwardVc';

import AdminHome from "@/adminPages/AdminHome";
import UserAdminVc from "@/adminPages/UserAdminVc";
import ArticleAdminVc from "@/adminPages/ArticleAdminVc";
import { useTokenStore } from '@/store/mytoken';
import path from 'path-browserify';
import ThesisVc from '@/textPages/ThesisVc';
import { componentSizeMap } from 'element-plus';
import AdminStatColumn from '@/adminPages/AdminStatColumn.vue';
import InformationVc from '@/textPages/InformationVc';
import InformationDetailVc from "@/textPages/InformationDetailVc.vue"
import AdminImageManager from '@/adminPages/AdminHomeImage.vue';
import AdminData from "@/adminPages/AdminData.vue"
import SSUBlastVc from '@/pages/SSUBlastVc.vue';
import gp60BlastVc from '@/pages/gp60BlastVc.vue';
import ResourceShareVc from '@/components/ResourceShareVc.vue';
import AdminTeamImage from '@/adminPages/AdminTeamImage.vue';

import TeamMemberAdminVc from '@/adminPages/TeamMemberAdminVc.vue';
import ScienceVc from '@/components/ScienceVc.vue'
import DataVisualizationManage from '@/adminPages/DataVisualizationManage.vue';



const routes = [
  {
    path: '/login',
    component: LoginVc,
    name: "login",
  },
  {
    path: '/admin',
    component: AdminAppLayout,
    meta: { title: '后台管理' },
    children: [
      { path: 'homeadmin', component: AdminHome, meta: { title: '通知管理' }},
      { path:'imagemanager',component:AdminImageManager,meta:{title:'图片管理'} },
      { path: 'useradmin', component: UserAdminVc, meta: { title: '用户管理' } },
      { path: 'articlestat', component: AdminStatColumn, meta: { title: '统计表' } },
      { path: 'article', component: ArticleAdminVc, meta: { title: '文章' } },
      { path: 'dataadmin', component: AdminData, meta: { title: '数据管理' } },
      { path: 'teamImageadmin', component: AdminTeamImage, meta: { title: '团队照片管理' } },
      { path: 'teammemberadmin', component: TeamMemberAdminVc, meta: { title: '团队成员管理' } },
      { path: 'teamImageadmin', component: AdminTeamImage, meta: { title: '团队照片管理' } },
      { path: 'dataVisualizationManage', component: DataVisualizationManage, meta: { title: '数据可视化管理' } },
    ]
  },
  {
    path: '/',
    component: AppLayout,
    meta: { title: "系统" },
    children: [
      {
        path: 'home', component: HomeVc, meta: { title: '首页' },
        children:[
          {
            path:'information',
            component:InformationVc,
            meta:{title:'通知'},
            children:[
              {
                path:'informationdetail',
                component:InformationDetailVc,
                meta:{title:"通知详情"}
              }
            ]
          }
        ]
      },
      {
        path:'datasubmit',component:DataSubmitVc,
      },
      {path:'SSUrRNA',component:SSUBlastVc},
      {path:'gp60',component:gp60BlastVc},
      {path:'mafft',component:MafftVc},
      {path:'teamIntroduction',component:TeamIntroductionVc},
      {path:'resourceshare',component:ResourceShareVc},
      {path:'datavisualization',component:DataVisualizationVc},
      {path:'notifications',component:InformationVc,
        children:[
          {
            path:'notificationdetail',
            component:InformationDetailVc,
            meta:{title:"通知详情"}
          }
        ]
      },
      {path:'peoplecenter',component:PeopleCenterVc},
      {path:'science',component:ScienceVc}
    ]
  }
];




const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.matched.some(r => r.meta.requiresAuth)) {
    console.log("守卫")
    const store = useTokenStore()
    console.log("store.token", store.token.access_token)
    if (!store.token.access_token) {
      next({ name: "login", query: { redirect: to.fullPath } })
    } else {
      next()
    }
  } else {
    next()
  }

})

export default router;
