<template>
    <el-timeline>
        <el-timeline-item v-for="(activity, index) in activities" :key="index" :icon="activity.icon"
            :type="activity.type" size="large" :timestamp="activity.timestamp" placement="top">
            <el-card class="timeline-card">
                <span>{{ activity.content }}</span>
            </el-card>
        </el-timeline-item>
    </el-timeline>
</template>
<script>
export default {
    name: "TopicTimeLine",
    data() {
        return {

            // 时间线
            activities: [{
                content: '查阅资料、调研，完成开题报告',
                timestamp: '2023-01-07 23:59',
                type: 'success',
                icon: 'el-icon-success'
            }, {
                content: '中期检查',
                timestamp: '2023-03-10 23:59',
                type: 'warning',
                icon: 'el-icon-more'
            }, {
                content: '第一稿检查',
                timestamp: '2023-03-25 23:59',
                type: 'warning',
                icon: 'el-icon-more'
            }, {
                content: '第二稿检查',
                timestamp: '2023-04-15 23:59',
                type: 'warning',
                icon: 'el-icon-more'
            }, {
                content: '终稿审核、结题验收',
                timestamp: '2023-05-8 23:59',
                type: 'warning',
                icon: 'el-icon-more'
            }, {
                content: '毕业答辩',
                timestamp: '2023-05-15 23:59',
                type: 'warning',
                icon: 'el-icon-more'
            }]
        }
    }
}
</script>
<style scoped lang="less">
/deep/.el-timeline-item__timestamp.is-top {
    color: black;
    line-height: 2;
    font-size: 18px;
    text-align: left;
}

.timeline-card {
    background-color: rgba(240, 248, 255, 0.7);
    backdrop-filter: blur(10px);
    font-size: 18px;
}
</style>