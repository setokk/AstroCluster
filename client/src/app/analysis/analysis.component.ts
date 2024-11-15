import { Component } from '@angular/core';
import { DropdownListComponent } from '../dropdown-list/dropdown-list.component';

@Component({
  selector: 'app-analysis',
  standalone: true,
  imports: [ DropdownListComponent ],
  templateUrl: './analysis.component.html',
  styleUrl: './analysis.component.css'
})
export class AnalysisComponent {
  gitUrl: string = '';
  lang: string = '';
  selectedLang: string = '';
  languages = [
    { label: 'Java', value: 'java' }
  ];

  constructor() {}

  onLanguageChange(lang: string) {
    this.selectedLang = lang;
  }

  onSubmit() {
    console.log({
      gitUrl: this.gitUrl,
      lang: this.lang
    });
  }
}
