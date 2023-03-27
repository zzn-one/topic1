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
import jwtPrase from '@/assets/js/jwtPrase';

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
            genderList: [],
            collegeList: [],
            majorList: [],
            titleList: [],
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
        },
        // 初始化页面
        async initPage() {
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
                "职称",
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
            ]

            //根据jwt获取用户类型
            let payload = jwtPrase()
            const userType = payload.userType

            //根据用户类型 选择 labelList
            if (userType == '0') {
                //毕业生
                this.labelList = studentLabelList

                //根据jwt里的id 获取用户信息
                await this.getStudentMsg(payload.id)

            } else if (userType == '1') {
                //导师
                this.labelList = tutorLabelList

                //根据jwt里的id 获取用户信息
                await this.getTutorMsg(payload.id)

            } else if (userType == '2') {
                //管理员
                this.labelList = adminLabelList

                //根据jwt里的id  获取用户信息
                await this.getAdminMsg(payload.id)

            }

            this.$forceUpdate()

        },

        //根据jwt里的id  获取用户信息(管理员)
        async getAdminMsg(aId) {
            const resp = await this.$http.get('/userAdmin/' + aId)
            if (resp.data.code === 200) {
                let user = resp.data.data
                this.valueList[0] = user.aid
                this.valueList[1] = user.aname
                this.valueList[2] = user.dictGender.description
                this.valueList[3] = user.dictCollege.description
                this.valueList[4] = user.phone
                this.valueList[5] = user.email
            }
        },
        //根据jwt里的id  获取用户信息(导师)
        async getTutorMsg(tId) {
            const resp = await this.$http.get('/userTutor/' + tId)
            if (resp.data.code === 200) {

                let user = resp.data.data

                this.valueList[0] = user.tid
                this.valueList[1] = user.tutorName
                this.valueList[2] = user.dictTutorTitle.description
                this.valueList[3] = user.dictGender.description
                this.valueList[4] = user.dictCollege.description
                this.valueList[5] = user.dictMajor.description + "系"
                this.valueList[6] = user.phone
                this.valueList[7] = user.email
                this.valueList[8] = user.office
            }
        },
        //根据jwt里的id  获取用户信息(毕业生)
        async getStudentMsg(sId) {
            const _this = this
            const resp = await this.$http.get('/userStudent/' + sId)
            if (resp.data.code === 200) {

                let user = resp.data.data

                this.valueList[0] = user.sid
                this.valueList[1] = user.sname
                this.valueList[2] = user.dictGender.description
                this.valueList[3] = user.dictCollege.description
                this.valueList[4] = user.dictMajor.description
                this.valueList[5] = user.graduationYear
                this.valueList[6] = user.phone
                this.valueList[7] = user.email
            }
            //根据sid去获取topic

            this.$http.get("/topic/selected/" + sId).then((topicResp) => {
                if (topicResp.data.code === 200 && topicResp.data.data !== null) {
                    _this.valueList[8] = topicResp.data.data.topicName

                    let tId = topicResp.data.data.createdBy
                    _this.$http.get("/userTutor/" + tId).then((resp) => {
                        if (resp.data.code === 200 && resp.data.data !== null) {
                            const tutor = resp.data.data
                            //根据tId去获取 指导老师信息
                            _this.valueList[9] = tutor.tutorName
                            _this.valueList[10] = tutor.phone
                            _this.valueList[11] = tutor.email
                            _this.valueList[12] = tutor.office
                            _this.$forceUpdate()

                        }

                    })

                }
            })
        },

    },
    created() {
        this.initPage()

    },

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
    background-color: rgba(255, 255, 255, 0.6);
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