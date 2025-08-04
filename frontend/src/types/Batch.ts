export interface Batch {
  date: string,
  product: string,
  batch: number,
  operator: string,
  start_time: string,
  end_time: string
}

export interface BatchInput{
  product: string,
  operator: string,
  start_time: string,
  end_time: string
}

// Date	Product	Batch	Operator	Start Time	End Time
