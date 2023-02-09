import {StatusEnum} from './statusEnum';

export type TestStatus = 0 | 1 | 2 | 3 | 4 | 5;

/**
 * 任务状态.
 */
export const TEST_STATUS = {
  WaitIssue: {
    value: 0 as TestStatus,
    description: '待发布',
    clazz: 'danger'
  } as StatusEnum<TestStatus>,
  Issuing: {
    value: 1 as TestStatus,
    description: '发布中',
    clazz: 'warning'
  } as StatusEnum<TestStatus>,
  WaitReview: {
    value: 2 as TestStatus,
    description: '待评阅',
    clazz: 'dark'
  } as StatusEnum<TestStatus>,
  Reviewing: {
    value: 3 as TestStatus,
    description: '评阅中',
    clazz: 'success'
  } as StatusEnum<TestStatus>,
  finished: {
    value: 4 as TestStatus,
    description: '已完成',
    clazz: 'primary'
  } as StatusEnum<TestStatus>,
  Archived: {
    value: 5 as TestStatus,
    description: '已归档',
    clazz: 'info'
  } as StatusEnum<TestStatus>,
};
