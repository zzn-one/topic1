<template>
    <div class="main">
        <!-- 如果学生已有课题，显示 -->
        <template v-if="hasTopic">
            <div class="task-div">
                <h1>{{ year }}届毕业设计选题</h1>
                <h2>已结束</h2>
            </div>
        </template>
        <!-- 如果学生没有课题 且 没任务 ，显示 -->
        <template v-else-if="!hasTopic & !hasTask">
            <div class="task-div">
                <h1>当前没有选题任务</h1>
            </div>
        </template>
        <template v-else>
            <div class="task-div">
                <el-descriptions :column="1" :title="taskParams.taskName">
                    <el-descriptions-item label="院系">{{ taskParams.college }}</el-descriptions-item>
                    <el-descriptions-item label="专业">{{ taskParams.major }}</el-descriptions-item>
                    <el-descriptions-item label="开始时间">{{ taskParams.startTime }}</el-descriptions-item>
                    <el-descriptions-item label="选题时长">{{ taskParams.duration }}分钟</el-descriptions-item>
                    <el-descriptions-item label="距离任务开始还有">
                        <el-statistic format="HH:mm:ss" :value="new Date(taskParams.startTime).getTime()" time-indices
                            @finish="openSelectTopicBtn"></el-statistic>
                    </el-descriptions-item>
                </el-descriptions>
                <el-row>
                    <el-button type="primary" plain @click="openTopic">预览课题</el-button>
                    <el-button type="success" plain @click="topicSelect" :disabled="this.btnDisabled">进入选题</el-button>
                </el-row>
                <div style="height:20px"></div>
            </div>
        </template>

        <!-- 毕业设计时间线 -->
        <TopicTimeLine></TopicTimeLine>
        <!-- 预览课题页面 -->
        <el-dialog width="900px" title="预览课题页面" :visible.sync="previewDialogVisible">
            <TopicPreview></TopicPreview>
        </el-dialog>
    </div>
</template>

<script>
import TopicPreview from '@/components/home/student/TopicPreview.vue';
import TopicTimeLine from '@/components/home/shared/TopicTimeLine.vue';

export default {
    name: "TopicTask",
    components: {
        TopicTimeLine,
        TopicPreview
    },
    data() {
        return {
            hasTask: false,
            hasTopic: false,
            year: '',
            btnDisabled: true,
            taskParams: {
                taskName: '',
                college: '',
                major: '',
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
        }

    },
    created() {
        //选题任务
        this.taskParams.taskName = "选题任务名1"
        this.taskParams.college = "xxx学院"
        this.taskParams.major = "计算机科学与技术"
        this.taskParams.startTime = "2022-12-15 18:55"
        this.taskParams.duration = 100

        //是否有选题任务
        this.hasTask = true

        //该学生是否已有课题
        this.hasTopic = false

        //毕业年份
        this.year = 3022

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