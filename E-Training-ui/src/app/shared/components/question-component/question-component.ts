import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { ResponseComponent } from './response-component';
import { Response } from 'src/app/interfaces/response';
import { ButtonComponent } from '../button-component/button-component';
import { FormsModule } from '@angular/forms';
import { QuestionType } from '../../enums/question.types';
import { Question } from 'src/app/interfaces/question';

@Component({
  selector: 'app-question',
  standalone: true,
  imports: [CommonModule, ResponseComponent, ButtonComponent, FormsModule ],
  templateUrl: './question-component.html',
  styleUrl: './question-component.css'
})
export class QuestionComponent implements OnInit, OnChanges {
  

  @Input() id: number;
  @Input() order: number;
  @Input() type: string;
  @Input() description: string;
  
  @Input() responses: Response[];
  @Input() answer:string;

  @Output() onsave = new EventEmitter<any>()
  @Output() ondelete = new EventEmitter<any>()
  @Output() onback = new EventEmitter<any>()
  @Output() onnext = new EventEmitter<any>()
  
  typeInternal: string;
  responsesInternal: Response[];
  showResponses: boolean = false;   

  descriptionInternal: string = '';
  answerInternal: string = ''; 
  
  ngOnInit(): void {
    this.descriptionInternal = this.description;
    this.typeInternal = this.type;
    this.responsesInternal = this.responses? this.responses:[];
    this.answerInternal = this.answer? this.answer: '';

    if (this.responsesInternal.length > 0) {
      let answers: string[] = this.answer.split(",").filter(x => x !== '');    
      this.responsesInternal.forEach(r => {
        r.checked = answers.indexOf(r.order.toString()) > -1;
      });
    }

    this.showResponses = this.shouldShowResponses();
  }
  
  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }

  // type events
  typechange($event) {
    this.typeInternal = $event.target.value;   
    this.showResponses = this.shouldShowResponses();    
    this.answerInternal = '';
    //this.save();
  }

  // text events
  textchange($event) {
    this.descriptionInternal = $event.target.value;    
    //this.save();
  }

  answerchange($event) {
    this.answerInternal = $event.target.value;
    //this.save();
  }

  // Response events
  addResponse() {
    if (this.responsesInternal.filter(r => r.description === '').length > 0) {
      alert("Rellene primero las respuestas vacias");
    } else {
      this.responsesInternal.push({order:this.responsesInternal.length +1, description:'', checked: false});
      //this.save();
    }
  }

  saveResponse(response) {
    this.responsesInternal.map(o => {if (o.order === response.order) {
      o.description = response.text;
    }});
    //this.save();
  }
  
  removeResponse(response:Response) {
    this.responsesInternal = this.responsesInternal.filter(o => o.order !== response.order);
    for (let i = 0; i < this.responsesInternal.length; i++) {
      this.responsesInternal[i].order = i+1;
    }
    this.responseChangeSelection({order: response.order, checked: false});
  }

  responseChangeSelection(selection) {
    if (this.typeInternal === QuestionType.MONOSELECTION) {
      this.answerInternal = selection.order.toString();
      this.responsesInternal.forEach(r => {
        if (r.order === selection.order) {
          r.checked = true;
        } else {
          r.checked = false;
        }
      })
    } else if (this.typeInternal === QuestionType.MULTISELECTION) {
      if (selection.checked) {
        let orders =  this.answerInternal.split(",").filter(x => x !== '');
        orders.push(selection.order.toString());
        this.answerInternal = orders.join(",");
      } else {
        let orders = this.answerInternal.split(",").filter(x => x !== '' && x !== selection.order.toString());        
        this.answerInternal = orders.join(",");
      }
      let answers = this.answerInternal.split(",").filter(o => o !== '');
      this.responsesInternal.forEach(r => {
        if (answers.indexOf(r.order.toString()) > -1) {
          r.checked = true;
        } else {
          r.checked = false;
        }
      })
    }    
    //this.save();
  }

  get types(): string[] {
    return Object.values(QuestionType);
  }

  get responsesList(): Response[] {
    return this.responsesInternal
  }

  cleanResponses() {
    this.responsesInternal = this.responsesInternal.filter(r => r.description !== '');
  }

  validateQuestion() {
    return this.typeInternal !== '' && this.descriptionInternal !== '' && this.answerInternal !== '' && (!this.shouldShowResponses() || this.responsesInternal.length > 0);
  }

  save() {    
    let responses = [];

    //if (this.validateQuestion()) {
      
      if (this.shouldShowResponses()) {    
        for (let i = 0; i < this.responsesInternal.length; i++) {
          this.responsesInternal[i].order = i +1;
        }      
        responses = this.responsesInternal;      
        let answers = this.answerInternal.split(",").filter(res => (Number(res) <= this.responsesInternal.length));
        this.answerInternal = answers.join(",");
      }

      let question = {
        id: this.id,
        type: this.typeInternal,
        description: this.descriptionInternal,
        responses,
        answer: this.answerInternal,
        order: this.order
      };

      //alert(JSON.stringify(question));
      this.onsave.emit(question);
    
  }

  ondeletepush() {
    this.typeInternal = QuestionType.FREETEXT;
    this.descriptionInternal = '';
    this.showResponses = this.shouldShowResponses();
    this.answerInternal = '';
    this.ondelete.emit({order: this.order});
  }

  onbackpush() {
    this.cleanResponses();
    this.onback.emit(<Question>{
      id: this.id,
      type: this.typeInternal,
      order: this.order,
      description: this.descriptionInternal,
      answer: this.answerInternal,
      responses: this.responsesInternal
    });
  }

  onnextpush() {
    this.cleanResponses();
    this.onnext.emit(<Question>{
      id: this.id,
      type: this.typeInternal,
      order: this.order,
      description: this.descriptionInternal,
      answer: this.answerInternal,
      responses: this.responsesInternal
    });
  }

  shouldShowResponses(): boolean {
    return this.typeInternal === QuestionType.MONOSELECTION.toString() || this.typeInternal === QuestionType.MULTISELECTION.toString()
  } 

}


