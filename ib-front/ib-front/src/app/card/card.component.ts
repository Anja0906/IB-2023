import {Component, Input} from '@angular/core';
import {Certificate} from "../model";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})

export class CardComponent {
  @Input() certificate : Certificate = {
    id : 1,
    issuedAt : new Date(),
    type : "ROOT",
    serialNumber : "awjdjaskdjsa",
    validTo : new Date(),
  };
  issuedAt: string = "";
  validTo: string = "";

  ngOnInit() {
    // @ts-ignore
    this.issuedAt = this.certificate.issuedAt[2] + "." + (this.certificate.issuedAt[1]) + "." + this.certificate.issuedAt[0] + ".";
    // @ts-ignore
    this.validTo = this.certificate.validTo[2] + "." + (this.certificate.validTo[1]-1) + "." + this.certificate.validTo[0] + ".";
  }
}
