<template>
    <div class="main">

        <template v-if="hasTask">
            <!-- 如果task的status为3，显示结束 -->
            <template v-if="finished">
                <div class="task-div">
                    <h1>{{ taskParams.year }}届毕业设计选题</h1>
                    <h2>已结束</h2>
                </div>
            </template>
            <!-- 如果没有task 或 task的状态为2 ，显示 -->
            <template v-else>
                <div class="task-div">
                    <el-descriptions :column="1" :title="taskParams.name">
                        <el-descriptions-item label="院系">{{ taskParams.college }}</el-descriptions-item>
                        <el-descriptions-item label="专业">{{ taskParams.major }}</el-descriptions-item>
                        <el-descriptions-item label="开始时间">{{
                                taskParams.startTime.toLocaleString()
                        }}</el-descriptions-item>
                        <el-descriptions-item label="选题时长">{{ taskParams.duration }}分钟</el-descriptions-item>
                        <el-descriptions-item label="距离任务开始还有">
                            <el-statistic format="HH:mm:ss" :value="taskParams.startTime.getTime()" time-indices
                                @finish="openSelectTopicBtn"></el-statistic>
                        </el-descriptions-item>
                    </el-descriptions>
                    <el-row>
                        <el-button type="primary" plain @click="openTopic">预览课题</el-button>
                        <el-button type="success" plain @click="topicSelect"
                            :disabled="this.btnDisabled">进入选题</el-button>
                    </el-row>
                    <div style="height:20px"></div>
                </div>
            </template>
        </template>
        <template v-else>
            <div class="task-div">
                <h1>当前没有选题任务</h1>
            </div>
        </template>

        <!-- 预览课题页面 -->
        <el-dialog width="900px" title="预览课题页面" :visible.sync="previewDialogVisible">
            <TopicPreview></TopicPreview>
        </el-dialog>
    </div>
</template>

<script>
import jwt_prase from '@/assets/js/jwtPrase';
import TopicPreview from '@/components/home/student/TopicPreview.vue';

export default {
    name: "TopicTask",
    components: {
        TopicPreview
    },
    data() {
        return {
            hasTask: false,
            finished: false,
            btnDisabled: true,
            taskParams: {
                name: '',
                college: '',
                major: '',
                year: "",
                startTime: '',
                duration: ''
            },
            previewDialogVisible: false,
        }
    },
    methods: {
        topicSelect() {
            const date = new Date(this.taskParams.startTime)
            const deadline = date.getTime() + parseInt(this.taskParams.duration) * 60 * 1000
            this.$router.push({
                path: "/home/student/topicSelect",
                query: {
                    "deadline": deadline
                }
            })
        },
        openTopic() {
            // 发送axios 请求 
            // 根据 学生的专业  获取所有课题
            this.previewDialogVisible = true
        },
        openSelectTopicBtn() {
            this.btnDisabled = false
        },
        //获取任务 根据 year college major
        async getTask() {
            //先获取用户信息
            const userResp = await this.$http.get("/userStudent/" + jwt_prase().id)
            if (userResp.data.code === 200) {
                let year = userResp.data.data.graduationYear
                let major = userResp.data.data.major

                const taskResp = await this.$http.get("/task/running/" + major + "/" + year )

                console.log(taskResp);

                if (taskResp.data.code === 200 && taskResp.data.data !== null) {
                    //有task
                    this.hasTask = true
                    this.taskParams = taskResp.data.data

                    if (this.taskParams.status === 0) {
                        //任务未开始
                        this.finished = false
                    } else if (this.taskParams.status === 1) {
                        //任务执行中
                        this.finished = false

                    } else if (this.taskParams.status === 2) {
                        //任务取消，即无任务
                        this.hasTask = false
                    } else if (this.taskParams.status === 3) {
                        //任务完成
                        this.finished = true
                    }

                    //处理数据
                    const collegeResp = await this.$http.get("/dict/college/" + this.taskParams.college)
                    if (collegeResp.data.code === 200) {
                        this.taskParams.college = collegeResp.data.data.description
                    }
                    const majorResp = await this.$http.get("/dict/major/" + this.taskParams.major)
                    if (majorResp.data.code === 200) {
                        this.taskParams.major = majorResp.data.data.description
                    }

                    this.taskParams.startTime = new Date(this.taskParams.startTime)

                } else {
                    //无task
                    this.hasTask = false
                }
            }
        }

    },
    created() {
        //获取选题任务
        this.getTask()

    }
}
</script>
<style scoped lang="less">
.main {
    line-height: 24px;
}

.el-timeline {
    margin-top: 20px;
}

.task-div {
    border: 1px solid rgb(185, 186, 189);
    width: 400px;
    margin: 20px auto;
    background-color: rgba(240, 248, 255, 0.7);
    backdrop-filter: blur(10px);
}

/deep/.el-statistic .con {
    display: flex;
    justify-content: left;
    align-items: center;
}

/deep/.el-descriptions__title {
    font-size: 18px;
    font-weight: 700;
    margin: 0 auto;
    margin-top: 20px;
}

/deep/.el-descriptions {
    box-sizing: border-box;
    font-size: 18px;
    color: #303133;
    margin-bottom: 20px;

}

/deep/.el-descriptions-item__label:not(.is-bordered-label) {
    margin-right: 10px;
    margin-left: 10px;
    width: 200px;
    color: #000000;
}

/deep/.el-descriptions__body {
    background-color: rgba(255, 255, 255, 0.1);
    backdrop-filter: blur(10px);
}

/deep/.el-dialog {

    background: rgba(255, 255, 255, 0.9);

}
</style>