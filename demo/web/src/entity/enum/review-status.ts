import {StatusEnum} from './statusEnum';

export type ReviewStatus = 0 | 1 | 2 | 3;

/**
 * 作业状态.
 */
export const REVIEW_STATUS = {
  new: {
    value: 0 as ReviewStatus,
    description: '待评阅',
    clazz: 'warning'
  } as StatusEnum<ReviewStatus>,
  reviewing: {
    value: 1 as ReviewStatus,
    description: '评阅中',
    clazz: 'primary'
  } as StatusEnum<ReviewStatus>,
  reviewed: {
    value: 2 as ReviewStatus,
    description: '已评阅',
    clazz: 'success'
  } as StatusEnum<ReviewStatus>,
  end: {
    value: 3 as ReviewStatus,
    description: '已结束',
    clazz: 'info'
  } as StatusEnum<ReviewStatus>
};
