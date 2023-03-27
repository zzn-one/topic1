import Vue from 'vue';
import Router from 'vue-router';

import Home from '@/components/home/main/Home.vue';
import Login from '@/components/login/Login.vue';
import UserMsg from '@/components/home/shared/UserMsg.vue';
import TopicTask from '@/components/home/student/TopicTask.vue';
import TopicSelect from '@/components/home/student/TopicSelect.vue';
import StudentTopic from '@/components/home/student/StudentTopic.vue';
import TutorTopic from '@/components/home/tutor/TutorTopic.vue';
import TutorStudent from '@/components/home/tutor/TutorStudent.vue';
import InfoStudent from '@/components/home/admin/InfoStudent.vue';
import InfoTutor from '@/components/home/admin/InfoTutor.vue';
import TaskPublish from '@/components/home/admin/TaskPublish.vue';
import TopicCheck from '@/components/home/admin/TopicCheck.vue';
import TopicDisplay from '@/components/home/admin/TopicDisplay.vue';
import TopicTimeLine from '@/components/home/shared/TopicTimeLine.vue';

Vue.use(Router)

const router = new Router({

    routes: [
        { path: "/", redirect: "/login" },
        { path: "/login", component: Login },
        {path:'/home/admin',redirect:'/home/admin/taskPublish'},
        {path:'/home/student',redirect:'/home/student/topicTask'},
        {path:'/home/tutor',redirect:'/home/tutor/tutorTopic'},
        {
            path: "/home",
            name:'home',
            component: Home,
            children:[{
                path:"shared/userMsg",
                name:'userMsg',
                component:UserMsg
            },{
                path:"student/topicTask",
                component:TopicTask
            },{
                path:"student/topicSelect",
                component:TopicSelect
            },{
                path:"student/studentTopic",
                component:StudentTopic
            },{
                path:"tutor/tutorTopic",
                component:TutorTopic
            },{
                path:"tutor/tutorStudent",
                component:TutorStudent
            },{
                path:"admin/infoStudent",
                component:InfoStudent
            },{
                path:"admin/infoTutor",
                component:InfoTutor
            },{
                path:"admin/taskPublish",
                component:TaskPublish
            },{
                path:"admin/topicCheck",
                component:TopicCheck
            },{
                path:"admin/topicDisplay",
                component:TopicDisplay
            },{
                path:"shared/topicTimeLine",
                name:'timeLine',
                component:TopicTimeLine
            }
        ]
        },

    ],
})


export default router