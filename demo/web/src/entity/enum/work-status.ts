import {StatusEnum} from './statusEnum';

export type WorkStatus = 0 | 1 | 2 | 3 | 4;

/**
 * 作业状态.
 */
export const WORK_STATUS: {[index: string]: StatusEnum<WorkStatus>} = {
  UnSubmitted: {
    value: 0 as WorkStatus,
    description: '未提交',
    clazz: 'danger'
  } as StatusEnum<WorkStatus>,
  Submitted: {
    value: 1 as WorkStatus,
    description: '已提交',
    clazz: 'primary'
  } as StatusEnum<WorkStatus>,
  Reviewing: {
    value: 2 as WorkStatus,
    description: '评阅中',
    clazz: 'warning'
  } as StatusEnum<WorkStatus>,
  Reviewed: {
    value: 3 as WorkStatus,
    description: '已评阅',
    clazz: 'success'
  } as StatusEnum<WorkStatus>,
  end: {
    value: 4 as WorkStatus,
    description: '已结束',
    clazz: 'info'
  } as StatusEnum<WorkStatus>,
};
