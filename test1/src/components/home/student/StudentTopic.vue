<template>
    <div>
        <el-descriptions class="margin-top" title="我的毕业设计课题" :column="1" border>
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
    </div>
</template>

<script>
import jwt_prase from '@/assets/js/jwtPrase'

export default {
    name: "StudentTopic",
    components: {

    },
    methods: {
        async getStudentMsg() {
            let sId = jwt_prase().id
            const _this = this
            //根据sid去获取topic
            this.$http.get("/topic/selected/" + sId).then((topicResp) => {

                console.log(topicResp);

                if (topicResp.data.code === 200 && topicResp.data.data !== null) {
                    const topic = topicResp.data.data
                    _this.valueList[0] = topic.topicName
                    _this.valueList[1] = topic.dictTopicType.description
                    _this.valueList[2] = topic.dictTopicSource.description

                    let tId = topicResp.data.data.createdBy
                    _this.$http.get("/userTutor/" + tId).then((resp) => {
                        if (resp.data.code === 200 && resp.data.data !== null) {
                            const tutor = resp.data.data
                            //根据tId去获取 指导老师信息
                            _this.valueList[3] = tutor.tutorName
                            _this.valueList[4] = tutor.dictTutorTitle.description
                            _this.valueList[5] = tutor.phone
                            _this.valueList[6] = tutor.email
                            _this.valueList[7] = tutor.office
                            _this.$forceUpdate()

                        }

                    })

                } else {
                    for (let i = 0; i < 8; i++) {
                        _this.valueList.push("你尚未选择课题")
                    }

                }
            })
        },
    },
    data() {
        const LabelList = ["课题名称",
            "课题类型",
            "课题来源",
            "指导老师",
            "指导老师职称",
            "指导老师手机号",
            "指导老师邮箱",
            "指导老师办公室"
        ]

        return {
            labelList: LabelList,
            valueList: []
        }
    },
    created() {
        //根据sId获取课题信息,再根据课题信息中的tId获取导师信息
        this.getStudentMsg()

    }

}
</script>

<style scoped lang="less">
/deep/.el-descriptions__header {
    line-height: 100px;
}

.main {
    background-color: rgba(255, 255, 255, 0.7);
}

/deep/.el-descriptions__body .el-descriptions__table {
    height: 700px;

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
</style>