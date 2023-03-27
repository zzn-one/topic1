<template>
    <div class="main">
        <el-descriptions class="margin-top" title="用户信息" :column="2" border>
            <template slot="extra">
                <el-button plain type="warning" @click="showUpdateMsg">修改信息</el-button>
                <el-button plain type="warning" @click="showUpdatedPass">修改密码</el-button>
            </template>
            <template v-for="label, index  in labelList ">
                <el-descriptions-item>
                    <template slot="label">
                        <i class="el-icon-s-data"></i>
                        {{ label }}
                    </template>
                    {{ valueList[index] }}
                </el-descriptions-item>
            </template>
        </el-descriptions>
        <UpdatePassword :visible="updatedPassDialogVisible" @close="updateDialogClose"></UpdatePassword>
        <UpdateUserMsg :visible="updatedMsgDialogVisible" @close="updateMsgDialogClose"></UpdateUserMsg>

    </div>
</template>
<script>

import UpdatePassword from '@/components/home/shared/UpdatePassword.vue';
import UpdateUserMsg from '@/components/home/shared/UpdateUserMsg.vue';

export default {
    name: "UserMsg",
    components: {
        UpdatePassword,
        UpdateUserMsg
    },
    data() {
        return {
            labelList: [],
            valueList: [],
            updatedPassDialogVisible: false,
            updatedMsgDialogVisible: false
        }
    },
    methods: {
        showUpdatedPass() {
            this.updatedPassDialogVisible = true
        },
        showUpdateMsg() {
            this.updatedMsgDialogVisible = true
        },
        updateDialogClose(val) {
            this.updatedPassDialogVisible = val
        },
        updateMsgDialogClose(val) {
            this.updatedMsgDialogVisible = val
        }
    },
    created() {
        const studentLabelList = ["学号",
            "姓名",
            "性别",
            "学院",
            "专业",
            "毕业年份",
            "手机号",
            "邮箱",
            "毕业课题",
            "指导老师",
            "指导老师手机号",
            "指导老师邮箱",
            "指导老师办公室"
        ]
        const tutorLabelList = ["工号",
            "姓名",
            "性别",
            "所属学院",
            "所属系",
            "手机号",
            "邮箱",
            "办公室"
        ]
        const adminLabelList = ["工号",
            "姓名",
            "性别",
            "所属学院",
            "手机号",
            "邮箱",
            "办公室"
        ]

        //根据jwt获取用户类型
        const userType = 2

        //根据用户类型 选择 labelList
        if (userType == '0') {
            //毕业生
            this.labelList = studentLabelList

            //发送请求获取用户信息
            this.valueList = [
                "300015210227",
                "假名字",
                "假性别",
                "假学院",
                "假专业",
                "假年份",
                "假手机",
                "假邮箱",
                "假课题",
                "假老师",
                "假手机",
                "zzz02902@163.com",
                "这里是假的办公室"
            ]

        } else if (userType == '1') {
            //导师
            this.labelList = tutorLabelList

            //发送请求获取用户信息
            this.valueList = [
                "300015210227",
                "假名字",
                "假性别",
                "假学院",
                "假专业",
                "假手机",
                "假邮箱",
                "这里是假的办公室"
            ]

        } else if (userType == '2') {
            //管理员
            this.labelList = adminLabelList

            //发送请求获取用户信息
            this.valueList = [
                "300015210227",
                "假名字",
                "假性别",
                "假学院",
                "假手机",
                "假邮箱",
                "这里是假的办公室"
            ]
        } else {
            return;
        }

    }
}
</script>

<style scoped lang="less">
.main {
    line-height: 80px;
    background-color: rgba(255, 255, 255, 0.7);
}

/deep/.el-descriptions__body .el-descriptions__table {
    height: 720px;

}

/deep/.el-descriptions__body {
    background-color: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
}

/deep/.el-descriptions {
    font-size: 18px;
}

/deep/.el-descriptions__title {
    font-size: 24px;
}

/deep/.el-descriptions-item__label.is-bordered-label {
    background-color: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    color: black;
}

/deep/.el-button {
    font-size: 18px;
}
</style>