import { Component } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { DropdownListComponent } from '../dropdown-list/dropdown-list.component';
import { SupportedLanguagesEnum } from '../core/enums/supported-languages-enum';
import { ClusteringParadigmsEnum } from '../core/enums/clustering-paradigms-enum';
import { ActivatedRoute } from '@angular/router';
import { ClusterService } from '../services/cluster-service';
import {PerformClusteringRequest} from "../core/request/PerformClusteringRequest";
import {PerformClusteringResponse} from "../core/response/PerformClusteringResponse";


@Component({
  selector: 'app-analysis',
  standalone: true,
  imports: [ DropdownListComponent, FormsModule ],
  templateUrl: './analysis.component.html',
  styleUrl: './analysis.component.css'
})
export class AnalysisComponent {
  performClusteringRequest: PerformClusteringRequest = {
    gitUrl: '',
    lang: '',
    clusteringParadigm: '',
    isAsync: false,
    email: undefined
  };

  supportedLanguages = SupportedLanguagesEnum.entries();
  clusteringParadigms = ClusteringParadigmsEnum.entries();

  constructor(private route: ActivatedRoute, private clusterService: ClusterService) {}

  onDropdownChange(field: string, value: string) {
    (this.performClusteringRequest as any)[field] = value; // Dynamically update form model
  }

  onSubmit(form: NgForm): void {
    this.performClusteringRequest.gitUrl = form.form.value.gitUrl;
    this.performClusteringRequest.email = (form.form.value.email == '') ? undefined : form.form.value.email;
    this.performClusteringRequest.isAsync = form.form.value.isAsync;

    this.clusterService.performClustering(this.performClusteringRequest).subscribe({
      next: (response: PerformClusteringResponse) => {

      },
      error: (error) => {
        window.alert(`Status: ${error.status}\nErrors: ${error.error.errors.join(',\n')}`);
      }
    });
  }
}
