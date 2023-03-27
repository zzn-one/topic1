<template>
    <div class="first-title">
        <template v-for="(menu, index) in menuList">
            <el-submenu  :index="index + 1 + ''">
                <template slot="title">
                    <i class="el-icon-s-flag"></i>
                    {{ menu.title }}
                </template>

                <template v-for="child in menu.children">
                    <el-menu-item class="second-title">
                        <router-link :to="child.path" active-class="active">
                            {{ child.title }}
                        </router-link>
                    </el-menu-item>
                </template>
            </el-submenu>
        </template>
    </div>
</template>

<script>
import adminMenuList from '@/assets/js/adminMenu';
import tutorMenuList from '@/assets/js/tutorMenu';
import studentMenuList from '@/assets/js/studentMenu';


export default {
    name: "Menu",
    props: ["type"],
    data() {
        return {
            menuList: []
        };
    },
    methods: {
        loadMenu() {
            if (this.$props.type == "0") {
                this.menuList=studentMenuList
            } else if (this.$props.type == "1") {
                this.menuList=tutorMenuList
            } else if (this.$props.type == "2"){
                this.menuList=adminMenuList
            }
            
        }
    },
    mounted(){
        this.loadMenu()
    }
};
</script>

<style scoped lang="less">
/deep/.el-submenu__title {
    font-size: 18px;
}

.second-title {
    font-size: 16px;

}
a{
    text-decoration: none;
    color: rgb(0, 0, 0);
}
.active {
    color: rgb(240, 99, 18);
}

</style>
