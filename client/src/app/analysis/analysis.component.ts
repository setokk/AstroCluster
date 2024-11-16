import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { DropdownListComponent } from '../dropdown-list/dropdown-list.component';
import { SupportedLanguagesEnum } from '../enums/supported-languages-enum';
import { ClusteringParadigmsEnum } from '../enums/clustering-paradigms-enum';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-analysis',
  standalone: true,
  imports: [ DropdownListComponent, FormsModule ],
  templateUrl: './analysis.component.html',
  styleUrl: './analysis.component.css'
})
export class AnalysisComponent {
  formModel: any = {
    gitUrl: '',
    lang: '',
    clusteringParadigm: ''
  };

  supportedLanguages = SupportedLanguagesEnum.entries();
  clusteringParadigms = ClusteringParadigmsEnum.entries();

  constructor(private route: ActivatedRoute) {}

  onDropdownChange(field: string, value: string) {
    this.formModel[field] = value; // Dynamically update form model
  }

  onSubmit(form: NgForm): void {
    this.formModel.gitUrl = form.form.value.gitUrl;
    console.log(this.formModel);
  }
}
