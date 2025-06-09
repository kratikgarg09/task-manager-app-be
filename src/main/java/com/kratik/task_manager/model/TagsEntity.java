package com.kratik.task_manager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tags",schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagsEntity {
}
