import {Option} from "../components/select.component/option";

export enum QuestionType {
  FREETEXT = "FREETEXT",
  MONOSELECTION = "MONOSELECTION",
  MULTISELECTION = "MULTISELECTION" ,
  GAPS = "GAPS",
  CODE = "CODE"
}

export const questionTypes = (): Option[] => {
  return Object.values(QuestionType).map(q => <Option>{key: q, label: q});
}
