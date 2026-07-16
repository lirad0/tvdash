import { Component, Input } from '@angular/core';
import { CardModule } from 'primeng/card';

@Component({
  selector: 'generic-card',
  templateUrl: './generic-card.html',
  imports: [CardModule],
})

export class GenericCard {
  @Input() name!: string;
  @Input() img!: string;
  @Input() url!: string;
  editMode: boolean = false;
}
